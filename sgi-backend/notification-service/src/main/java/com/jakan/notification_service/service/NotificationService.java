package com.jakan.notification_service.service;

import com.jakan.notification_service.dto.NotificationDTO;
import com.jakan.notification_service.entity.Notification;
import com.jakan.notification_service.entity.model.EmailRequest;
import com.jakan.notification_service.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public NotificationDTO sendEmail(EmailRequest request) {
        // Envoyer l'email
        sendEmailNotification(request);

        // Sauvegarder la notification
        Notification notification = new Notification();
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setSubject(request.getSubject());
        notification.setContent(request.getContent());
        notification.setSentAt(LocalDateTime.now());
        notification.setRelatedIncidentId(request.getRelatedIncidentId());

        Notification saved = notificationRepository.save(notification);
        return convertToDTO(saved);
    }

    private void sendEmailNotification(EmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getRecipientEmail());
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    public List<NotificationDTO> getNotificationsByEmail(String email) {
        return notificationRepository.findByRecipientEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotifications(String email) {
        return notificationRepository.findByRecipientEmailAndIsReadFalse(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRecipientEmail(notification.getRecipientEmail());
        dto.setSubject(notification.getSubject());
        dto.setSentAt(notification.getSentAt());
        dto.setIsRead(notification.getIsRead());
        dto.setRelatedIncidentId(notification.getRelatedIncidentId());

        return dto;
    }
}
