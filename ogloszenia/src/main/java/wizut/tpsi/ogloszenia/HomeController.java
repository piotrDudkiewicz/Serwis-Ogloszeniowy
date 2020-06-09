/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wizut.tpsi.ogloszenia.component.UserSession;
import wizut.tpsi.ogloszenia.jpa.BodyStyle;
import wizut.tpsi.ogloszenia.jpa.CarManufacturer;
import wizut.tpsi.ogloszenia.jpa.CarModel;
import wizut.tpsi.ogloszenia.jpa.FuelType;
import wizut.tpsi.ogloszenia.jpa.Offer;
import wizut.tpsi.ogloszenia.jpa.User;
import wizut.tpsi.ogloszenia.services.OffersService;
import wizut.tpsi.ogloszenia.services.UserService;
import wizut.tpsi.ogloszenia.web.OfferFilter;

/**
 *
 * @author Pioterk
 */
@Controller
public class HomeController {

    @Autowired
    private OffersService offersService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserSession currentUser;

    @GetMapping("/")
    public String home(Model model, OfferFilter offerFilter, @RequestParam("page") Optional<Integer> page) {
        List<CarManufacturer> carManufacturers = offersService.getCarManufacturers();
        List<CarModel> carModels = null;
        List<FuelType> fuelTypes = offersService.getFuelTypes();

        if (offerFilter.getModelId() != null) {
            carModels = offersService.getCarModels(offerFilter.getManufacturerId());
        } else if (offerFilter.getManufacturerId() != null) {
            carModels = offersService.getCarModels(offerFilter.getManufacturerId());
        }

        int actual = page.orElse(1);

        Page<Offer> offers = offersService.getOffers(offerFilter, PageRequest.of(actual - 1, 20));

        int totalPages = offers.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pages = new ArrayList<>(totalPages - actual + 1);

            for (int i = 1; i <= totalPages; i++) {
                pages.add(i);
            }
            model.addAttribute("pagesList", pages);
        }

        model.addAttribute("carManufacturers", carManufacturers);
        model.addAttribute("carModels", carModels);
        model.addAttribute("offers", offers);
        model.addAttribute("fuelTypes", fuelTypes);
        return "offersList";
    }

    @GetMapping("/offer/{id}")
    public String offerDetails(Model model, @PathVariable("id") Integer id) {
        Offer offer = offersService.getOffer(id);
        model.addAttribute("offer", offer);
        return "offerDetails";
    }

    @GetMapping("/newoffer")
    public String newOfferForm(Model model, Offer offer) {
        if (currentUser.getUsername() != null) {
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();

            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);
            model.addAttribute("header", "Nowe ogłoszenie");
            model.addAttribute("action", "/newoffer");
        } else {
            model.addAttribute("error", "Zaloguj się!");
        }

        return "offerForm";
    }

    @PostMapping("/newoffer")
    public String saveNewOffer(Model model, @Valid Offer offer, BindingResult binding) {
    offer.setUser(userService.getUserByName(currentUser.getUsername()));
        if (binding.hasErrors()) {
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();

            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);
            model.addAttribute("header", "Nowe ogłoszenie");
            model.addAttribute("action", "/newoffer");

            return "offerForm";
        }
        offer = offersService.createOffer(offer);

        return "redirect:/offer/" + offer.getId();
    }

    @RequestMapping("/deleteoffer/{id}")
    public String deleteOffer(Model model, @PathVariable("id") Integer id) {
        Offer offer = offersService.deleteOffer(id,currentUser.getId());

        if (offer != null) {
            model.addAttribute("offer", offer);
        } else {
            model.addAttribute("error", "Nie możesz edytować tego ogłoszenia");
        }

        return "deleteOffer";
    }

    @GetMapping("/editoffer/{id}")
    public String editOffer(Model model, @PathVariable("id") Integer id) {

        Offer offer = offersService.getOffer(id);

        if (offer.getUser().getId() == currentUser.getId()) {
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();
            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);
            model.addAttribute("offer", offer);
            model.addAttribute("header", "Edycja ogłoszenia");
            model.addAttribute("action", "/editoffer/" + id);
        } else {
            model.addAttribute("error", "Nie możesz edytować tego ogłoszenia");
        }

        return "offerForm";
    }

    @PostMapping("/editoffer/{id}")
    public String saveEditedOffer(Model model, @PathVariable("id") Integer id, @Valid Offer offer, BindingResult binding) {
        if (binding.hasErrors()) {
            model.addAttribute("header", "Edycja ogłoszenia");
            model.addAttribute("action", "/editoffer/" + id);

            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();

            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);

            return "offerForm";
        }
        offersService.saveOffer(offer);

        return "redirect:/offer/" + offer.getId();
    }
}
