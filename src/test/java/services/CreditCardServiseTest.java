package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;

@ContextConfiguration(locations = "classpath:spring/junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreditCardServiseTest extends AbstractTest {

    @Autowired
    private CreditCardService	creditCardService;

    @Autowired
    private ActorService		actorService;

    @Autowired
    private UserService userService;


    // Tests ------------------------------------------------------------------

    public void creditCardEdit(final String username, final String holder, final String brand,
                               final String number, final Integer expirationMonth, final Integer expirationYear,
                               final Integer cvv, String creditCardBean,  final Class<?> expected) {

        Class<?> caught = null;

        try {

            this.authenticate(username);

            // Inicializamos los atributos para la edición
            CreditCard creditCard;
            creditCard = creditCardService.findOne(super.getEntityId(creditCardBean));

            creditCard.setHolder(holder);
            creditCard.setBrand(brand);
            creditCard.setNumber(number);
            creditCard.setExpirationMonth(expirationMonth);
            creditCard.setExpirationYear(expirationYear);
            creditCard.setCvv(cvv);

            // Guardamos
            this.creditCardService.save(creditCard);
            creditCardService.flush();

            this.unauthenticate();

        } catch (final Throwable oops) {

            caught = oops.getClass();

        }

        this.checkExceptions(expected, caught);
    }

    public void creditCardCreate(final String username, final String holder, final String brand, final String number, final Integer expirationMonth, final Integer expirationYear, final Integer cvv, final Class<?> expected) {

        Class<?> caught = null;

        try {

            this.authenticate(username);

            // Inicializamos los atributos para la edición
            final CreditCard creditCard = this.creditCardService.create();

            creditCard.setHolder(holder);
            creditCard.setBrand(brand);
            creditCard.setNumber(number);
            creditCard.setExpirationMonth(expirationMonth);
            creditCard.setExpirationYear(expirationYear);
            creditCard.setCvv(cvv);

            // Guardamos
            this.creditCardService.save(creditCard);
            creditCardService.flush();
            this.unauthenticate();

        } catch (final Throwable oops) {

            caught = oops.getClass();

        }

        this.checkExceptions(expected, caught);
    }

    public void creditCardDelete(final String username, String creditCardBean, final Class<?> expected) {
        Class<?> caught = null;

        try {

            this.authenticate(username);

            // Inicializamos los atributos para la edición
            final CreditCard creditCard;

            creditCard = creditCardService.findOne(super.getEntityId(creditCardBean));

            // Borramos
            this.creditCardService.delete(creditCard);
            creditCardService.flush();

            this.unauthenticate();

        } catch (final Throwable oops) {

            caught = oops.getClass();

        }

        this.checkExceptions(expected, caught);
    }
    // Drivers ----------------------------------------------------------------

    @Test
    public void driverCreditCardEdit() {

        final Object testingData[][] = {
                // Editar credit card de su user y atributos correctos (todos) -> true
                {
                        "user1", "holder", "brand", "4532865767277390", 12, 2020, 100, "creditCard1", null
                },
                // Editar credit card con administrador autentificado -> false
                {
                        "admin", "holder", "brand", "4532865767277390", 12, 2020, 100,"creditCard1", IllegalArgumentException.class
                },
                // Editar credit card sin autentificar -> false
                {
                        null, "holder", "brand", "4532865767277390", 12, 2020, 100,"creditCard1", IllegalArgumentException.class
                },
                // Editar credit card con atributos incorrectos -> false
                {
                        "user1", "holder", "brand", "4532865767277390", null, 2020, 100,"creditCard1", ConstraintViolationException.class
                },
                // Editar credit card con un script -> false
                {
                        "user1", "<script>", "brand", "4532865767277390", 12, 2020, 100,"creditCard1", ConstraintViolationException.class
                },

        };
        for (int i = 0; i < testingData.length; i++)
            this.creditCardEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (String) testingData[i][7],
                    (Class<?>) testingData[i][8]);
    }
    @Test
    public void driverCreateCreditCard() {

        final Object testingData[][] = {

                // Crear creditCard sin autentificar -> false
                {
                        null, "holder", "brand", "4532865767277390", 12, 2020, 100, IllegalArgumentException.class
                },
                // Crear creditCard con atributos válidos -> true
                {
                        "user1", "holder", "brand", "4532865767277390", 12, 2020, 100, null
                },
                // Crear creditCard con atributos inválidos -> false
                {
                        "user1", "holder", "brand", "4532865767277391", 12, 2022, 500, ConstraintViolationException.class
                },
                // Crear creditCard autenticado como manager -> false
                {
                        "manager1", "holder", "brand", "4532865767277390", 12, 2022, 500, IllegalArgumentException.class
                },

        };
        for (int i = 0; i < testingData.length; i++)
            this.creditCardCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Class<?>) testingData[i][7]);
    }

    @Test
    public void driverDeleteCreditCard() {

        final Object testingData[][] = {
                // Borrar una creditCard logueado como usuario sin ser su creditCard -> false
                {
                        "user1", "creditCard2", IllegalArgumentException.class
                },
                // Borrar una creditCard sin estar logueado -> false
                {
                        null, "creditCard1", IllegalArgumentException.class
                },
                // Borrar una creditCard logueado como manager -> false
                {
                        "manager1", "creditCard1", IllegalArgumentException.class
                },
                // Borrar una creditCard estando logueado como usuario con su creditCard -> true
                {
                        "user1", "creditCard1", null
                },

        };
        for (int i = 0; i < testingData.length; i++)
            this.creditCardDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }
}