package ru.mirea.ovsyannikovvi.pkmn;

import java.io.IOException;

public class PkmnApplication {
    public static void main(String[] args) throws IOException {
        CardImport cardimport = new CardImport();
        Card card;
        card = cardimport.Deserialize("C:\\Users\\Ovsyanka\\IdeaProjects\\pkmn\\src\\main\\resources\\Croagunk.crd");
        //card = cardimport.Fill("C:\\Users\\Ovsyanka\\IdeaProjects\\pkmn\\src\\main\\resources\\my_card.txt");
        System.out.println(card.toString());
        //CardExport cardexport = new CardExport(card);
        //cardexport.Serialize();
    }
}
