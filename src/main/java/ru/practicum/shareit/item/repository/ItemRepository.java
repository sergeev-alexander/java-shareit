package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String text,
            String sameText);

    Optional<Item> findByIdAndOwnerId(Long itemId, Long ownerId);

    void deleteByOwner(Long ownerId);

    default Item getItemById(Long itemId) {
        return findById(itemId).orElseThrow(() -> new NotFoundException("There's no item with id " + itemId));
    }

}
