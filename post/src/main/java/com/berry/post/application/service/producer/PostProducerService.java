package com.berry.post.application.service.producer;

import com.berry.post.application.model.event.PostEvent;

public interface PostProducerService {

  void sendPostCreateEvent(PostEvent.Close event);

  void sendPostUpdateEvent(PostEvent.Update event);

}
