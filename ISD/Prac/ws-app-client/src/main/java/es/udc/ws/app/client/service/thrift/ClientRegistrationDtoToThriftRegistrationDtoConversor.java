package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientRegistrationDto;
import es.udc.ws.app.thrift.ThriftRegistrationDto;

import java.util.ArrayList;
import java.util.List;

public class ClientRegistrationDtoToThriftRegistrationDtoConversor {

    // De ClientRegistrationDto a ThriftRegistrationDto
    public static ThriftRegistrationDto toThriftRegistrationDto(ClientRegistrationDto clientDto) {
        return new ThriftRegistrationDto(
                clientDto.getRegistrationId() == null ? -1 : clientDto.getRegistrationId(),
                clientDto.getCourseId(),
                clientDto.getEmail(),
                clientDto.getCardNumber(),
                clientDto.getRegistrationDate(),
                clientDto.getCancellationDate() == null ? "" : clientDto.getCancellationDate()
        );
    }

    // De ThriftRegistrationDto a ClientRegistrationDto
    public static ClientRegistrationDto toClientRegistrationDto(ThriftRegistrationDto thriftDto) {
        return new ClientRegistrationDto(
                thriftDto.getRegistrationId() == -1 ? null : thriftDto.getRegistrationId(),
                thriftDto.getCourseId(),
                thriftDto.getEmail(),
                thriftDto.getCardNumber(),
                thriftDto.getRegistrationDate(),
                thriftDto.getCancellationDate()
        );
    }

    // Lista de ThriftRegistrationDto a ClientRegistrationDto
    public static List<ClientRegistrationDto> toClientRegistrationDtos(List<ThriftRegistrationDto> thriftDtos) {
        return thriftDtos.stream()
                .map(ClientRegistrationDtoToThriftRegistrationDtoConversor::toClientRegistrationDto)
                .toList();
    }
}
