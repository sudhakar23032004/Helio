package com.helio.data.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ChatMessageResponseMap {

    private LocalDateTime createdTimestamp;

    List<ChatMessageResponse> chatMessageResponseList;

}
