/*
*    Copyright [2015] [wisemapping]
*
*   Licensed under WiseMapping Public License, Version 1.0 (the "License").
*   It is basically the Apache License, Version 2.0 (the "License") plus the
*   "powered by wisemapping" text requirement on every single page;
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the license at
*
*       http://www.wisemapping.org/license
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package com.wisemapping.webmvc;

import com.wisemapping.model.User;
import com.wisemapping.security.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Value("${database.driver}")
    private String driver;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    protected ModelAndView showLoginPage() {
        final User user = Utils.getUser(false);
        ModelAndView result;
        if (user != null) {
            result = new ModelAndView("forward:/c/maps/");
        } else {
            result = new ModelAndView("login");
            result.addObject("isHsql", driver.contains("hsql"));
        }
        return result;
    }

    @RequestMapping(value = "loginopenid", method = RequestMethod.GET)
    protected ModelAndView showLoginOpenIdPage() {
        final User user = Utils.getUser(false);
        ModelAndView result;
        if (user != null) {
            result = new ModelAndView("forward:/c/maps/");
        } else {
            result = new ModelAndView("loginopenid");
        }
        return result;
    }

}
