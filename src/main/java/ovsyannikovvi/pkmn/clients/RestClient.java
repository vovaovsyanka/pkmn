package ovsyannikovvi.pkmn.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestClient {

    private final RestTemplate restTemplate;

    public PokemonCardResponse getCardByName(String cardName) {
        String url = "https://api.pokemontcg.io/v2/cards?q=name:" + cardName;
        return restTemplate.getForObject(url, PokemonCardResponse.class);
    }
}
