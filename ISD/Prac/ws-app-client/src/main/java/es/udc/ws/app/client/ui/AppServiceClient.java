package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientService;
import es.udc.ws.app.client.service.ClientServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientRegistrationDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public class AppServiceClient {

    private static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if (expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    private static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    private static void printUsage() {
        System.err.println("""
            Usage:
                [addCourse] AppServiceClient -addCourse <city> <name> <startDate> <price> <maxPlaces>
                [findCourses] AppServiceClient -findCourses <city>
                [findCourse] AppServiceClient -findCourse <courseId>
                [registerForCourse] AppServiceClient -register <courseId> <email> <paymentCard>
                [cancelRegistration] AppServiceClient -cancelRegistration <registrationId> <email>
                [findRegistrations] AppServiceClient -findRegistrations <email>
            """);
    }

    private static void showError(Exception e, String errorMessage) {
        System.out.print("\n\033[31m\033[1m" + errorMessage + "\033[0m:\n" + e.getMessage() + "\n\033[31m\033[1mStack trace\033[0m:\n");
        e.printStackTrace();
        System.out.print("\n");
    }

    private static void showSuccess(String successMessage, String optionalText) {
        System.out.print("\n\033[32m\033[1m" + successMessage + "\033[0m");
        if (optionalText != null) {
            System.out.print(optionalText);
        }
    }


    public static void main(String[] args) {

        if (args.length == 0) {
            printUsageAndExit();
        }

        ClientService clientService = ClientServiceFactory.getService();

        // add course
        if ("-addCourse".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[]{4, 5});

            try {
                ClientCourseDto course = clientService.addCourse(new ClientCourseDto(
                        null, // courseId
                        args[1], // city
                        args[2], // name
                        args[3], // startDate
                        Double.parseDouble(args[4]), // price
                        Integer.parseInt(args[5]), // maxPlaces
                        Integer.parseInt(args[5]) // availablePlaces, equal to maxPlaces by default
                ));

                showSuccess("Successfully added course", ":\n");
                System.out.println(course);
                System.out.println();

            } catch (InputValidationException e) {
                showError(e, "Could not add course");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // find courses by city
        else if ("-findCourses".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});

            try {
                List<ClientCourseDto> courses = clientService.findCourses(
                        args[1] // city
                );

                final String s = courses.size() == 1 ? "" : "s";
                showSuccess("Found " + courses.size() + " course" + s + " in " + args[1], ":\n");
                courses.forEach(System.out::println);
                System.out.println();

            } catch (InputValidationException e) {
                showError(e, "Could not find courses");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // find course by id
        else if ("-findCourse".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{1});

            try {
                ClientCourseDto course = clientService.findCourse(
                        Long.parseLong(args[1]) // courseId
                );

                showSuccess("Found course with id " + course.getCourseId(), ":\n");
                System.out.println(course);
                System.out.println();

            } catch (InstanceNotFoundException e) {
                showError(e, "Could not find course");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // inscribe to a course
        else if ("-register".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[]{2});

            try {
                ClientRegistrationDto registration = clientService.registerForCourse(
                        args[1], // email
                        Long.parseLong(args[2]), // courseId
                        args[3] // cardNumber
                );

                showSuccess("Successfully registered for course with id " + registration.getCourseId(), ":\n");
                System.out.println(registration);
                System.out.println();

            } catch (InputValidationException | InstanceNotFoundException | RegistrationOutOfDeadlineException | FullCourseException | EmailAlreadyRegisteredException e) {
                showError(e, "Could not register for course");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // cancel registration
        else if ("-cancelRegistration".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[]{1});

            try {
                clientService.cancelRegistration(
                        Long.parseLong(args[1]), // registrationId
                        args[2] // email
                );

                showSuccess("Successfully cancelled registration with id " + args[1], null);
                System.out.println();
                System.out.println();

            } catch (InstanceNotFoundException | CancellationEmailDoesNotMatchException | CancellationOutOfDeadlineException | RegistrationAlreadyCancelledException e) {
                showError(e, "Could not cancel registration");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // find registrations
        else if ("-findRegistrations".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});

            try {
                List<ClientRegistrationDto> registrations = clientService.findAllRegistrationsFrom(
                        args[1] // email
                );

                final String s = registrations.size() == 1 ? "" : "s";
                showSuccess("Found " + registrations.size() + " registration" + s + " from " + args[1], ":\n");
                registrations.forEach(System.out::println);
                System.out.println();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // invalid argument
        else {
            printUsageAndExit();
        }
    }
}
