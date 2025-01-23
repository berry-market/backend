package com.berry.post.application.service.producer;

import com.berry.post.application.model.event.PostEvent;
import com.berry.post.application.model.event.PostEvent.Status;

public interface PostProducerService {

  void sendPostCloseEvent(PostEvent.Status event);

  void sendPostActiveEvent(PostEvent.Status event);

}
