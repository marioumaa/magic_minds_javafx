package Service;
import com.mailjet.client.errors.MailjetException;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
public class MailjetService {
    private final String apiKey ="402bc36dc5c7453753538d3dc1fa6afb";
    private final String apiSecret ="1abb28a768835f0ac9a1bbffff669354";
    public void sendMailjet(String to, String subject, String content) throws MailjetException {
        MailjetRequest request;
        MailjetResponse response;

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecret)
                .build();


        MailjetClient client = new MailjetClient(options);

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "benslimen.louay@esprit.tn")
                                        .put("Name", "no-replyMagic mind"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)
                                                .put("Name", "passenger 1")))
                                .put(Emailv31.Message.TEMPLATEID, 5929202)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT,subject)
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("content", content))));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
