package it.univaq.disim.mwt.postd.business.impl;

import it.univaq.disim.mwt.postd.business.AuthBO;
import it.univaq.disim.mwt.postd.business.BusinessException;
import it.univaq.disim.mwt.postd.domain.GroupClass;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.repository.jpa.GroupRepository;
import it.univaq.disim.mwt.postd.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = BusinessException.class)
public class AuthBOImpl implements AuthBO {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserClass user) throws BusinessException {
        GroupClass logged = groupRepository.findByName("logged").orElseThrow(BusinessException::new);

        user.setGroup(logged);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // user creation
        userRepository.save(user);
    }
}
