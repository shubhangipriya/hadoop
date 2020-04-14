package controller;

@Path("/users")
public class UserController {

    @Path("/all")
    @GET
    @Produces("application/json")
    public JsonArray getAll() {
        return Json.createdArrayBuilder().build();
    }



}
