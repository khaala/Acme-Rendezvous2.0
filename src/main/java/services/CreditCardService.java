package services;


import java.util.Collection;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import repositories.CreditCardRepository;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

    // Managed Repositories
    @Autowired
    private CreditCardRepository	creditCardRepository;

    // Supported Services

    @Autowired
    private ActorService			actorService;


    // Constructor
    public CreditCardService() {
        super();
    }

    // Simple CRUD Methods

    public CreditCard create() {

        Assert.isTrue(this.actorService.isUser());
        CreditCard result;

        result = new CreditCard();
        result.setActor(this.actorService.findByPrincipal());

        return result;
    }

    public CreditCard save(final CreditCard creditCard) {

        Assert.notNull(creditCard);
        Assert.isTrue(this.actorService.isUser());
        Assert.isTrue(creditCard.getActor().equals(this.actorService.findByPrincipal()));

        CreditCard result;

        result = this.creditCardRepository.save(creditCard);

        return result;
    }

    public void delete(final CreditCard creditCard) {
        Assert.notNull(creditCard);
        Assert.isTrue(this.actorService.isUser());
        Assert.isTrue(creditCard.getActor().equals(this.actorService.findByPrincipal()));

        this.creditCardRepository.delete(creditCard);
    }

    public CreditCard findOne(final int creditCardId) {
        Assert.isTrue(creditCardId != 0);
        CreditCard result;

        result = this.creditCardRepository.findOne(creditCardId);
        Assert.notNull(result);
        return result;
    }

    public Collection<CreditCard> findAll() {
        Collection<CreditCard> result;

        result = this.creditCardRepository.findAll();
        Assert.notNull(result);

        return result;
    }

    // Other business methods

    public CreditCard findOneByActorId(final int actorId) {

        CreditCard result;

        result = this.creditCardRepository.findOneByActorId(actorId);

        return result;

    }





}
