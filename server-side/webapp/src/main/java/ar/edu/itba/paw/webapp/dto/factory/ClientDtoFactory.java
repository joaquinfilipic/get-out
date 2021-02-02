package ar.edu.itba.paw.webapp.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.webapp.dto.model.profile.ClientDto;
import ar.edu.itba.paw.webapp.dto.model.profile.UserDto;

public class ClientDtoFactory {
	
	public static ClientDto buildFromClient(final Client client) {
		return new ClientDto()
				.id(client.getId())
				.user(new UserDto()
						.id(client.getUser().getId())
						.username(client.getUser().getUsername())
						.name(client.getUser().getName()))
				.gender(client.getGender())
				.avatar(client.getAvatar())
				.content(client.getContentType())
				.bio(client.getBio());
	}
	
	public static List<ClientDto> buildListFromClients(final List<Client> clients) {
		return clients.stream()
				.map(client -> buildFromClient(client))
				.collect(Collectors.toList());
	}
	
	public static ClientDto buildReducedFromClient(final Client client) {
		return new ClientDto()
				.id(client.getId())
				.user(new UserDto()
						.id(client.getUser().getId())
						.name(client.getUser().getName()));
	}
	
	public static List<ClientDto> buildReducedListFromClients(final List<Client> clients) {
		return clients.stream()
				.map(client -> buildReducedFromClient(client))
				.collect(Collectors.toList());
	}

}
