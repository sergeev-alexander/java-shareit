package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating user already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "User name field is blank!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating or updating user name field is bigger than 128 characters!")
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Email field is blank!")
    @Email(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class},
            message = "Wrong email format!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating or updating user email field is bigger than 128 characters!")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(getId(), userDto.getId())
                && Objects.equals(getName(), userDto.getName())
                && Objects.equals(getEmail(), userDto.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail());
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
