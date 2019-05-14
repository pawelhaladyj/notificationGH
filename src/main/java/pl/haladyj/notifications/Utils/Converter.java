package pl.haladyj.notifications.Utils;

public interface Converter <ENTITY, DTO> {
    ENTITY toEntity(DTO dto);
    DTO toDTO(ENTITY entity);
}
