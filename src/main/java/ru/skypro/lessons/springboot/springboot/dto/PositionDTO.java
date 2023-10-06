package ru.skypro.lessons.springboot.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.lessons.springboot.springboot.entity.Position;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PositionDTO {
    private Integer id;
    private String name;

    public static PositionDTO fromPosition(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setName(position.getName());
        return positionDTO;
    }

    public Position toPosition() {
        Position position = new Position();
        position.setId(this.getId());
        position.setName(this.getName());
        return position;
    }

}
