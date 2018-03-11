package services;

import domain.Question;
import domain.Rendezvous;
import domain.Requestt;
import domain.Servise;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@Transactional
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionTest extends AbstractTest {

    // The SUT ---------------------------------
    @Autowired
    private RequesttService requesttService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RendezvousService rendezvousService;



    public void questionCreateTest(String username, String rendezvousBean,String text,  Class<?> expected) {
        Class<?> caught=null;

        try {
            int rendezvousId = super.getEntityId(rendezvousBean);
            Rendezvous rendezvous = rendezvousService.findOne(rendezvousId);

            this.authenticate(username);

            Question result = questionService.create();
            result.setText(text);
            result.setRendezvous(rendezvous);

            questionService.save(result);
            this.unauthenticate();


        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
            this.checkExceptions(expected, caught);

    }

    // Drivers
    // ===================================================

    @Test
    public void driverQuestionCreateTest() {

        final Object testingData[][] = {
                // Crear una Question estando logueado como user -> true
                {
                        "user1", "rendezvous1","Question test?", null
                },
                // Crear una Question con texto -> false
                {
                        "user1", "rendezvous1",null,ConstraintViolationException.class // todo: Preguntar al profesor
                },
                // Crear una Question logueado como manager --> false
                {
                        "manager2", "rendezvous2","Question created by manager test?",IllegalArgumentException.class
                },
                // Crear una Quesion sin  loguearse -> false
                {
                        null, "rendezvous1", "Question anonymous Test", IllegalArgumentException.class
                }
        };
        for (int i = 0; i < testingData.length; i++)
            questionCreateTest((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2],(Class<?>) testingData[i][3]);

    }


}