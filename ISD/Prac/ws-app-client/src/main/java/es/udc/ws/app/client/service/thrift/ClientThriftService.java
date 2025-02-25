package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientService;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientRegistrationDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ClientThriftService implements ClientService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ClientThriftService.endpointAddress";

    private final static String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    private ThriftAppService.Client getClient() {

        try {
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftAppService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ClientCourseDto addCourse(ClientCourseDto course) throws InputValidationException {
        ThriftAppService.Client client = getClient();
        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            ThriftCourseDto thriftCourseDto = ClientCourseDtoToThriftCourseDtoConversor.toThriftCourseDto(course);
            ThriftCourseDto returnedCourse = client.addCourse(thriftCourseDto);
            return new ClientCourseDto(
                    returnedCourse.getCourseId(),
                    returnedCourse.getCity(),
                    returnedCourse.getName(),
                    LocalDateTime.ofEpochSecond(returnedCourse.getStartDate(), 0, ZoneOffset.UTC)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    returnedCourse.getPrice(),
                    returnedCourse.getMaxPlaces(),
                    returnedCourse.getAvailablePlaces()
            );
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCourseDto> findCourses(String keywords) {
        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return ClientCourseDtoToThriftCourseDtoConversor.toClientCourseDtos(client.findCourses(keywords));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientCourseDto findCourse(Long courseId) throws InstanceNotFoundException {
        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            ThriftCourseDto thriftCourseDto = client.findCourse(courseId);
            return ClientCourseDtoToThriftCourseDtoConversor.toClientCourseDtos(Arrays.asList(thriftCourseDto)).get(0);

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ClientRegistrationDto registerForCourse(String email, Long courseId, String paymentCard)
            throws InputValidationException, InstanceNotFoundException, RegistrationOutOfDeadlineException,
            FullCourseException, EmailAlreadyRegisteredException {

        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            // Llamada al servicio Thrift para registrar al usuario
            ThriftRegistrationDto thriftRegistrationDto = client.registerForCourse(email, courseId, paymentCard);

            // Conversión del DTO de Thrift a DTO de cliente
            return ClientRegistrationDtoToThriftRegistrationDtoConversor.toClientRegistrationDto(thriftRegistrationDto);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftRegistrationOutOfDeadlineException e) {
            throw new RegistrationOutOfDeadlineException(courseId, LocalDateTime.now());
        } catch (ThriftFullCourseException e) {
            throw new FullCourseException(courseId);
        } catch (ThriftEmailAlreadyRegisteredException e) {
            throw new EmailAlreadyRegisteredException(courseId, email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void cancelRegistration(Long registrationId, String email) throws InstanceNotFoundException, CancellationEmailDoesNotMatchException, CancellationOutOfDeadlineException, RegistrationAlreadyCancelledException {
        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            client.cancelRegistration(registrationId, email);

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftCancellationEmailDoesNotMatchException e) {
            throw new CancellationEmailDoesNotMatchException(registrationId, email);
        } catch (ThriftCancellationOutOfDeadlineException e) {
            throw new CancellationOutOfDeadlineException(registrationId);
        } catch (ThriftRegistrationAlreadyCancelledException e) {
            throw new RegistrationAlreadyCancelledException(registrationId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientRegistrationDto> findAllRegistrationsFrom(String email) {
        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientRegistrationDtoToThriftRegistrationDtoConversor.toClientRegistrationDtos(client.findAllRegistrationsFrom(email));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
