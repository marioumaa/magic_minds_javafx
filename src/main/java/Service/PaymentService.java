package Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

public class PaymentService {
    static {

        Stripe.apiKey = "sk_test_51M7hARHJtiyIQHNyyh2zWynv637KZhopDd0YIfeYv6VERix6VeWfqJ8wqbnF8dYO5irvoO1UeY9D8b7yWLkrrpGW00WTD7iDum";
    }
    public static boolean processPayment(double amountInUSD) {
        try {
            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount((long) (amountInUSD * 100)) // Convert dollars to cents
                    .setCurrency("usd")
                    .setDescription("Payment for order")
                    .setSource("tok_visa")  // Test token, replace in production
                    .build();
            Charge charge = Charge.create(params);
            return true;  // Assume the payment was successful if no exception was thrown
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
