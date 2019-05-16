package com.zk.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("springEvent")// 只有在springEvent环境下，该bean才会被激活
public class MyEventListener implements ApplicationListener {

  @Override
  public void onApplicationEvent(ApplicationEvent applicationEvent) {
    if (applicationEvent instanceof MyEvent) {
      // do some thing
      System.err.println("触发监听的事件：" + applicationEvent);
    } else {
      System.err.println("其他的事件");
    }
  }
}
