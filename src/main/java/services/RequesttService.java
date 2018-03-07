package services;

import domain.Requestt;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.RequesttRepository;

import java.util.Collection;

@Service
@Transactional
public class RequesttService {

    // Managed repository -----------------------------------------------------

    @Autowired
    private RequesttRepository requestRepository;

    // Managed services -----------------------------------------------------

    @Autowired
    private UserService userService;


    // Constructors -----------------------------------------------------------

    public RequesttService(){
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    public Requestt create(){
        Requestt result;

        result =  new Requestt();

        return result;
    }

    public Requestt findOne(Integer requestId){
        return requestRepository.findOne(requestId);
    }

    public Requestt save(Requestt request){
        User principal;
        Requestt request1;

        principal = userService.findByPrincipal();
        Assert.isTrue(principal.equals(request.getRendezvous().getCreator()),"must be the principal");
        request1 =requestRepository.save(request);

        return request1;
    }

    public Collection<Requestt> findAll(){
        return requestRepository.findAll();
    }

    public void delete(Requestt request){
        User principal;

        principal = userService.findByPrincipal();
       // Assert(principal.getUserAccount().getAuthorities().contains(Authority.ADMIN));

        requestRepository.delete(request);
    }

}