/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.demo.consumer;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.demo.DemoService;
import org.apache.dubbo.rpc.service.GenericService;

public class Application {
    public static void main(String[] args) throws Exception {
        if (isClassic(args)) {
            runWithRefer();
        } else {
            runWithBootstrap();
        }
        Thread.sleep(10000000L);
    }

    private static boolean isClassic(String[] args) {
        return args.length > 0 && "classic".equalsIgnoreCase(args[0]);
    }

    private static void runWithBootstrap() throws IOException {
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setInterface(DemoService.class);
        reference.setGeneric("false");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance(); // 创建DubboBootstrap，指定ApplicationConfig以及RegistryConfig
        bootstrap.application(new ApplicationConfig("dubbo-demo-api-consumer"))
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference)
                .start();

        DemoService demoService = ReferenceConfigCache.getCache().get(reference);
        // String message = demoService.sayHello("dubbo");
        CompletableFuture<String> dubbo = demoService.sayHelloAsync("dubbo");
        dubbo.whenComplete((s, throwable) -> System.out.println(s));
        System.in.read();

//        // generic invoke
//        GenericService genericService = (GenericService) demoService;
//        Object genericInvokeResult = genericService.$invoke("sayHello", new String[]{String.class.getName()},
//                new Object[]{"dubbo generic invoke"});
//        System.out.println(genericInvokeResult);
    }

    private static void runWithRefer() {
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        reference.setInterface(DemoService.class);
        DemoService service = reference.get();
        String message = service.sayHello("dubbo");
        System.out.println(message);
    }
}
