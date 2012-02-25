package bd.gov.forms.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author asif
*/
public class Role {

    private static final Logger log = LoggerFactory.getLogger(Role.class);

    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    public static String checkRole(int role, User user) {
        if (user == null) {
            log.debug("User is null");
            return "redirect:/userMgt/login.htm?message=msg.login.expired&msgType=failed";
        } else if (user.getAdmin() < role) {
            log.debug("Access denied for user: {}", user.getName());
            return "redirect:/formBuilder/done.htm?doneMessage=msg.access.denied&doneMsgType=failed";
        }

        log.debug("User role: {}", user.getAdmin());

        return null;
    }

}