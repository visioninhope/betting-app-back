package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalanceForUser(@PathVariable Integer userId) {

        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is missing");
        }
        return ApiResponseUtil.handleApiResponse(paymentService.getUserPaymentsApiResponse(userId));
    }

    @PostMapping("/deposit/{userId}/{amount}")
    public ResponseEntity<?> depositAmount(@PathVariable Integer userId, @PathVariable Double amount) {

        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is missing");
        }
        if (amount == null || amount.isNaN()) {
            return ResponseEntity.badRequest().body("Amount is missing");
        }
        return ApiResponseUtil.handleApiResponse(paymentService.addPaymentApiResponse(userId, amount, Constants.PAYMENT_DEPOSIT));
    }

    @PostMapping("/withdraw/{userId}/{amount}")
    public ResponseEntity<?> withdrawAmount(@PathVariable Integer userId, @PathVariable Double amount) {

        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is missing");
        }
        if (amount == null || amount.isNaN()) {
            return ResponseEntity.badRequest().body("Amount is missing");
        }
        return ApiResponseUtil.handleApiResponse(paymentService.addPaymentApiResponse(userId, -amount, Constants.PAYMENT_WITHDRAW));
    }
}
