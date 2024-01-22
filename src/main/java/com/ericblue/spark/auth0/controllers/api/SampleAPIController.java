package com.ericblue.spark.auth0.controllers.api;

import com.ericblue.spark.auth0.config.AppConfiguration;
import com.ericblue.spark.auth0.controllers.base.BaseController;
import com.ericblue.spark.auth0.controllers.response.RestResponse;
import com.ericblue.spark.auth0.controllers.response.RestResponseType;
import com.ericblue.spark.auth0.exception.AppJSONException;
import com.ericblue.spark.auth0.model.Message;
import com.ericblue.spark.auth0.utils.DataEncodeUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.ws.rs.*;

import static spark.Spark.get;
import static spark.Spark.post;


@Api(value = "Sample API", description = "Sample API Endpoints", tags = {" API"})
@Path("/api")
public class SampleAPIController extends BaseController {

    /**
     * Logger for this class
     */
    protected static final Logger logger = LoggerFactory.getLogger(SampleAPIController.class);

    public SampleAPIController() {

        logger.debug("Initializing Controller " + this.getClass().getName());
        this.defineRoutes();
    }

    public void defineRoutes() {

        get("/api/config", this::testGetConfiguration);

        get("/api/testget", this::testGet);

        post("/api/sendmessage", this::sendMessage);


    }

    @GET
    @ApiOperation(value = "GET Test", nickname = "testGet")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = "param1", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "RestResponse", response = RestResponse.class)
    })

    @Produces("application/json")
    @Path("/testget")


    public String testGet(@ApiParam(hidden = true) Request request,
                          @ApiParam(hidden = true) Response response) throws AppJSONException {


        response.status(200);
        response.type("application/json");

        String param1 = request.queryParams("param1");


        RestResponse jsonResponse = new RestResponse();
        jsonResponse.setResponseType(RestResponseType.SUCCESS);
        jsonResponse.setResponse("param1: " + param1);

        return DataEncodeUtils.dataToJson(jsonResponse);


    }


    @GET
    @ApiOperation(value = "Get Configuration", nickname = "gatConfiguration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "AppConfiguration", response = AppConfiguration.class)
    })

    @Produces("application/json")
    @Path("/config")


    public String testGetConfiguration(@ApiParam(hidden = true) Request request,
                                       @ApiParam(hidden = true) Response response) throws AppJSONException {

        response.status(200);
        response.type("application/json");

        AppConfiguration returnedConfig = getConfig();

        return DataEncodeUtils.dataToJson(returnedConfig);


    }

    @POST
    @ApiOperation(value = "Sends a message", nickname = "sendMessage")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = "text", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "ReturnedMessage", response = Message.class)
    })

    @Produces("application/json")
    @Path("/sendmessage")


    public String sendMessage(@ApiParam(hidden = true) Request request,
                              @ApiParam(hidden = true) Response response) throws AppJSONException {


        // Used to retrieve session objects
        // request.session().attribute("sessionVariable");

        response.status(200);
        response.type("application/json");

        String text = request.body();

        if (StringUtils.isEmpty(text)) {
            throw new AppJSONException("Parameter 'text' is required and can't be left blank");
        } else {

            Message message = new Message(text);
            Message responseMessage = sampleAPIService.sendMessage(message);

            return DataEncodeUtils.dataToJson(responseMessage);

        }

    }

}
