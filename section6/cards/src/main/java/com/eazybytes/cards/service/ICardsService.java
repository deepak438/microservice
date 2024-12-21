package com.eazybytes.cards.service;

import com.eazybytes.cards.dto.CardsDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public interface ICardsService {

    void createCard(String mobileNumber);

    CardsDto getCards(String mobileNumber);

    boolean updateCardDetails(CardsDto cardsDto);

    boolean deleteCardDetails(String mobileNumber);
}
