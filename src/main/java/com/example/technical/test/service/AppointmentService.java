package com.example.technical.test.service;

import com.example.technical.test.dao.IAppointmentDAO;
import com.example.technical.test.model.Appointment;
import com.example.technical.test.model.StateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class AppointmentService implements IAppointmentService {
    final String API_PERSIST = "http://localhost:8081/api/persistAppointments";
    List<Appointment> sentAppointments = new ArrayList<>();

    @Autowired
    IAppointmentDAO iAppointmentDAO;

    @Override
    public List<Appointment> getAppointments() {
        List<Appointment> appointments = iAppointmentDAO.getAppointments();
        return appointments;
    }

    @Override
    public List<Appointment> generateAppointments(int size) {
        List<Appointment> randomAppointments = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            boolean isNew = getRandomNew();
            if (isNew) {
                randomAppointments.add(generateNewAppointment());
            } else {
                randomAppointments.add(updateAppointment());
            }

        }

        return randomAppointments;
    }

    @Override
    public void setAppointments(List<Appointment> appointments) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        int size = appointments.size();
        int i = 0;
        int from = 0;
        int to = 5;
        while (from <= size) {
            if (to > size) {
                to = size;
            }
            List<Appointment> appointmentListForSend = appointments.subList(from, to);

            HttpEntity<List<Appointment>> entity = new HttpEntity<List<Appointment>>(appointmentListForSend, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> result = restTemplate.exchange(API_PERSIST, HttpMethod.POST, entity, String.class);
            addToListSentAppointments(result.getBody(), appointmentListForSend);
            if (!result.getStatusCode().equals(HttpStatus.OK)) {
                break;
            }
            from = from + 5;
            to = to + 5;

        }
    }

    private void addToListSentAppointments(String ids, List<Appointment> appointments) {
        List<Integer> idsList = toIdsListFromString(ids);

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (idsList.get(i) == null) {
                break;
            }

            long id = idsList.get(i).longValue();

            appointment.setId(idsList.get(i));
            if (sentAppointments.stream().filter(tmp -> tmp.getId() == id).findAny().isPresent()) {
                sentAppointments.removeIf(tmp -> tmp.getId() == id);
                sentAppointments.add(appointment);
            } else {
                sentAppointments.add(appointment);
            }

        }

    }

    public void runEvery30Sekond() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            System.out.println("30 sekund");
            p();
        };
        executor.scheduleWithFixedDelay(task, 0, 30000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setRandomAppointments() {
        runEvery30Sekond();
    }

    private void p() {
        List<Appointment> appointmentList = generateAppointments(30);

        setAppointments(appointmentList);
    }

    private List<Integer> toIdsListFromString(String json) {
        List<Integer> result = new ArrayList<Integer>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean getRandomNew() {
        List<Boolean> givenList = Arrays.asList(true, false);
        Random rand = new Random();
        boolean randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }

    private String getRandomState() {
        List<String> givenList = Arrays.asList("APPROVED", "NOT_APPROVED");
        Random rand = new Random();
        String randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }

    private String getRandomReason() {
        List<String> givenList = Arrays.asList("flu", "toothache", "advice");
        Random rand = new Random();
        String randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }

    private Appointment generateNewAppointment() {
        long personId = getRandomPersonId();
        String reason = getRandomReason();
        StateEnum stateEnum = StateEnum.valueOf(getRandomState());
        return new Appointment(new Date(), personId, new Date(), reason, stateEnum);
    }

    private Appointment updateAppointment() {
        Appointment randomAppoitment = getRandomAppointment();
        randomAppoitment.setModified(new Date());
        randomAppoitment.setReason(getRandomReason());
        randomAppoitment.setState(StateEnum.valueOf(getRandomState()));
        return randomAppoitment;
    }

    private Appointment getRandomAppointment() {
        if (sentAppointments.isEmpty()) {
            return null;
        }
        List<Appointment> givenList = sentAppointments;
        Random rand = new Random();
        Appointment randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }

    private long getRandomPersonId() {
        List<Long> givenList = Arrays.asList(Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Long.valueOf(4), Long.valueOf(5),
                Long.valueOf(6), Long.valueOf(7), Long.valueOf(8), Long.valueOf(9), Long.valueOf(10),
                Long.valueOf(11), Long.valueOf(12), Long.valueOf(13), Long.valueOf(14), Long.valueOf(15),
                Long.valueOf(16), Long.valueOf(17), Long.valueOf(18), Long.valueOf(19), Long.valueOf(20),
                Long.valueOf(21), Long.valueOf(22), Long.valueOf(23), Long.valueOf(24), Long.valueOf(25),
                Long.valueOf(26), Long.valueOf(27), Long.valueOf(28), Long.valueOf(29), Long.valueOf(30));
        Random rand = new Random();
        long randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }
}
