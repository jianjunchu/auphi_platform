package org.firzjb.admin.authorization.resolver;

import org.firzjb.authorization.repository.UserModelRepository;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.sys.model.response.UserResponse;
import org.firzjb.sys.service.IUserService;
import org.firzjb.utils.BeanCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hao
 * @create 2017-04-10
 */
@Component
public class UserRepository implements UserModelRepository {

    @Autowired
    private IUserService userService;

    @Override
    public Object getCurrentUser(String username) {
        UserResponse response = userService.get(username);
        CurrentUserResponse currentUser = null;
        if(response !=null){
            currentUser = BeanCopier.copy(response, CurrentUserResponse.class);
        }
        return  currentUser;
    }
}
