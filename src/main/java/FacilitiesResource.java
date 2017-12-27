import com.fasterxml.jackson.databind.ObjectMapper;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.enterprise.context.RequestScoped;
import com.kumuluz.ee.logs.cdi.Log;
import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("facilities")
@Log
public class FacilitiesResource {

    private Client httpClient;
    private ObjectMapper objectMapper;

    @Inject
    @DiscoverService(value = "apartments", version = "1.0.x", environment = "dev")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        objectMapper = new ObjectMapper();
    }

    @Inject
    private RestProperties restProperties;

    @GET
    public Response getApartmentsFilteredByFacilities(
            @QueryParam("list") final List<String> list) {

        for (String l : list)
            StringUtils.capitalize(l);

        System.out.println("list size: " + list.size());
        List<Apartment> apartments = null;

        Optional<Boolean> apartmentServiceEnabled = ConfigurationUtil.getInstance().getBoolean("rest-properties.external-services.apartment-service.enabled");
        System.out.println("----------------------" +apartmentServiceEnabled.get()+ "-------------------------");


        System.out.println("---------------" + restProperties.isApartmentServiceEnabled() + "---------------------");
        if(restProperties.isApartmentServiceEnabled())
            apartments = filterApartmentsByFacilities(list);

        return apartments != null
                ? Response.ok(apartments).build()
                : Response.status(Response.Status.NOT_FOUND).build();

    }

    public List<Apartment> filterApartmentsByFacilities(List<String> list){

        if(baseUrl.isPresent() && list.size() > 0) {
            try {
                String urlString = baseUrl.get() + "/v1/apartments/facilities?";
                for (String l : list){
                    urlString += "list=" + l + "&";
                }
                urlString = urlString.substring(0, urlString.length()-1);

                System.out.println(urlString);

                //url should be like: ?list=one&list=two&list=three
                return httpClient.target(urlString).request()
                        .get(new GenericType<List<Apartment>>() {});


            } catch (Exception e) {
                String msg = e.getClass().getName() + " occured: " + e.getMessage();
                throw new InternalServerErrorException(msg);
            }
        }
        else
            return new ArrayList<>();
    }

    private List<Apartment> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Apartment.class));
    }
}
