package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.OutgoingRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Transactional
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;
    private final ItemMapper itemMapper;
    private final RequestMapper requestMapper;

    @Override
    public Collection<OutgoingRequestDto> getAllRequesterRequests(Long requesterId, Pageable pageable) {
        userRepository.checkUserById(requesterId);
        List<Request> requestList = requestRepository.findByRequesterId(requesterId, pageable);
        Map<Long, List<OutgoingItemDto>> outgoingItemDtoMap = itemRepository.findByRequestIdIn(requestList
                .stream()
                .map(Request::getId)
                .collect(toList()))
                .stream()
                .map(itemMapper::mapItemToOutgoingDto)
                .collect(Collectors.groupingBy(OutgoingItemDto::getRequestId, toList()));
        return requestList
                .stream()
                .map(requestMapper::mapRequestToOutgoingDto)
                .peek(outgoingRequestDto -> outgoingRequestDto.setItems(outgoingItemDtoMap
                        .getOrDefault(outgoingRequestDto.getId(), List.of())))
                .collect(toList());
    }

    @Override
    public Collection<OutgoingRequestDto> getAllRequests(Long userId, Pageable pageable) {
        userRepository.checkUserById(userId);
        List<Request> requestList = requestRepository.findByRequesterIdIsNot(userId, pageable);
        Map<Long, List<OutgoingItemDto>> outgoingItemDtoMap = itemRepository.findByRequestIdIn(requestList
                        .stream()
                        .map(Request::getId)
                        .collect(toList()))
                .stream()
                .map(itemMapper::mapItemToOutgoingDto)
                .collect(Collectors.groupingBy(OutgoingItemDto::getRequestId, toList()));
        return requestList
                .stream()
                .map(requestMapper::mapRequestToOutgoingDto)
                .peek(outgoingRequestDto -> outgoingRequestDto.setItems(outgoingItemDtoMap
                        .getOrDefault(outgoingRequestDto.getId(), null)))
                .collect(toList());
    }

    @Override
    public OutgoingRequestDto getRequestById(Long userId, Long requestId) {
        userRepository.checkUserById(userId);
        Request request = requestRepository.findRequestById(requestId);
        List<OutgoingItemDto> itemList = itemRepository.findByRequestId(requestId)
                .stream()
                .map(itemMapper::mapItemToOutgoingDto)
                .collect(toList());
        OutgoingRequestDto outgoingRequestDto = requestMapper.mapRequestToOutgoingDto(request);
        outgoingRequestDto.setItems(itemList);
        return outgoingRequestDto;
    }

    @Override
    public OutgoingRequestDto postRequest(Long requesterId, IncomingRequestDto incomingRequestDto) {
        Request request = requestMapper.mapIncomingDtoToRequest(incomingRequestDto);
        request.setRequester(userRepository.getUserById(requesterId));
        return requestMapper.mapRequestToOutgoingDto(requestRepository.save(request));
    }

}
