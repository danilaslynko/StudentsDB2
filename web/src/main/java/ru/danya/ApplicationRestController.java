package ru.danya;

import ru.danya.dao.StudentDAO;
import ru.danya.entity.Student;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.sql.Date;
import java.util.List;

@Path("")
public class ApplicationRestController {

    @EJB(name = "java:global/ejb3/StudentDAO")
    StudentDAO studentDAO;

    @GET
    @Path("/showall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents() {
        List<Student> allStudents = studentDAO.getAllEntities();
        return Response.ok(allStudents).status(200).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(String studentJson) {
        try {
            Student student = getStudentFromJson(studentJson);
            if (studentDAO.addEntity(student)) {
                return Response.status(201).build();
            } else {
                return Response.serverError().build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(400, "Illegal date format").build();
        }
    }

    private Student getStudentFromJson(String studentJson) throws IllegalArgumentException {
        JsonReader jsonReader = Json.createReader(new StringReader(studentJson));
        JsonObject jsonObject = jsonReader.readObject();
        Student student = new Student(
                jsonObject.getString("name"),
                jsonObject.getString("surname"),
                jsonObject.getString("group"),
                Date.valueOf(jsonObject.getString("dateOfBirth"))
        );
        return student;
    }

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(String idJson) {
        JsonReader reader = Json.createReader(new StringReader(idJson));
        long id = reader.readObject().getInt("id");
        if (studentDAO.deleteEntity(id)) {
            return Response.status(201).build();
        } else {
            return Response.serverError().build();
        }
    }
}
