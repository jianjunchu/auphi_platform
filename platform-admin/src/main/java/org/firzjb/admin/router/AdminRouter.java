/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2018 by Auphi BI : http://www.doetl.com

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.firzjb.admin.router;

import org.firzjb.base.router.BaseRouter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-08-30 22:34
 */
@Controller
@RequestMapping(value = "{admin.path}")
public class AdminRouter extends BaseRouter {

    @Override
    protected String getPrefix() {
        return null;
    }

    /**
     * 通用邮箱验证页面
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public String active(String auth_code) {
        return "/active";
    }
}
