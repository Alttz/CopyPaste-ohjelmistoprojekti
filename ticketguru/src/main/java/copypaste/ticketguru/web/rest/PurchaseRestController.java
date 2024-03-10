package copypaste.ticketguru.web.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import copypaste.ticketguru.domain.Purchase;

import copypaste.ticketguru.domain.PurchaseRepository;
import copypaste.ticketguru.domain.PurchaseRequest;
import copypaste.ticketguru.service.PurchaseService;

@RestController
public class PurchaseRestController {

	@Autowired
    private PurchaseService purchaseService;
	@Autowired
	PurchaseRepository purchaseRepository;

	@GetMapping(value = "/api/purchases")
	public List<Purchase> getAllPurchases() {
		return (List<Purchase>) purchaseRepository.findAll();
	}
	
	// Luodaan ostotapahtuma, jossa voidaan ostaa lippuja useampaan tapahtumaan
	@PostMapping("/api/purchases")
    @Transactional
    public ResponseEntity<?> createMultiplePurchasesWithTickets(@RequestBody List<PurchaseRequest> purchaseRequests) {
        List<Purchase> successfulPurchases = new ArrayList<>();
        List<String> failedPurchases = new ArrayList<>();

		// Käsittelee jokaisen tapahtuman osto itsenäisesti
        purchaseRequests.forEach(request -> {
            Optional<Purchase> result = purchaseService.processPurchaseRequest(request);
            result.ifPresentOrElse(
                successfulPurchases::add,
                () -> failedPurchases.add("Ostotapahtumaa ei pystytty suorittamaan tapahtumaan ID:llä: " + request.getEventId() + ". Tarkasta tapahtuman ID, lipputyypit ja saatavuus.")
            );
        });
	    // Luo vastaus, jossa käydään läpi onnistuneet ja epäonnistuneet ostotapahtumat.
        return constructResponse(successfulPurchases, failedPurchases);
    }

    // Luodaan vastaus, sisöältäen onnistuneet ja epäonnistuneet ostotapahtumat
    private ResponseEntity<?> constructResponse(List<Purchase> successfulPurchases, List<String> failedPurchases) {
        Map<String, Object> response = new HashMap<>();
        response.put("successfulPurchases", successfulPurchases);
        response.put("failedPurchases", failedPurchases);
        return ResponseEntity.ok(response);
    }
}