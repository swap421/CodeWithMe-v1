package com.connectify.connectify.Service;

import com.connectify.connectify.Repository.ChatRoomRepository;
import com.connectify.connectify.Entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists){
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId,recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists){
                        var chatId = createChat(senderId,recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChat(String senderId, String recipientId) {
        var chatId = String.format("%s_%s",senderId,recipientId);
        ChatRoom recipientSender = new ChatRoom().builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
        ChatRoom senderRecipient = new ChatRoom().builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }
}
