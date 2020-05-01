package fii.practic.health.threads;

import fii.practic.health.entity.model.Doctor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/* We want to send an e-mail to a doctor. Multiple steps are needed for that:
        1. fetch template
        2. fetch doctor
        3. fill template
        4. send the email
*/
public class MyCompletableFuture {


    public static void main(String[] args) {
        // 1. fetch template
        CompletableFuture<String> template = CompletableFuture.supplyAsync(MyCompletableFuture::fetchTemplate);

        // 2. fetch doctor
        CompletableFuture<Doctor> doctor = CompletableFuture.supplyAsync(MyCompletableFuture::fetchDoctor);

        // 3. fill template with doctor's data
        CompletableFuture<String> filledTemplate = template.thenCombine(doctor, (t, d) -> String.format(t, d.getFirstName()));

        // send the email
        try {
            String email = filledTemplate.get();
            System.out.println(String.format("Seding this email: %s", email));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private static Doctor fetchDoctor() {
        takeNap(750);
        Doctor doctor = new Doctor();
        doctor.setFirstName("Bogdan");
        doctor.setLastName("Boca");
        return doctor;
    }

    private static String fetchTemplate() {
        takeNap(500);
        return "Hello Mr. %s";
    }

    private static void takeNap(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
