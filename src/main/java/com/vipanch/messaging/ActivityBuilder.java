package com.vipanch.messaging;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.schema.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityBuilder {

    public static Activity createCardActivity(String cardType) {
        // Create buttons
        CardAction button1 = new CardAction();
        button1.setType(ActionTypes.MESSAGE_BACK);
        button1.setTitle("MESSAGE_BACK");
        button1.setText("Text for button 1");
        button1.setValue("Button 1 Value");

        CardAction button2 = new CardAction();
        button2.setType(ActionTypes.IM_BACK);
        button2.setTitle("IM_BACK");
        button2.setText("Text for button 2");
        button2.setValue("Button 2 Value");

        CardAction button3 = new CardAction();
        button3.setType(ActionTypes.POST_BACK);
        button3.setText("Text for button 3");
        button3.setTitle("POST_BACK");
        button3.setValue("Button 3 Value");

        CardAction button4 = new CardAction();
        button4.setType(ActionTypes.INVOKE);
        button4.setText("Text for button 4");
        button4.setTitle("INVOKE");
        button4.setValue("Button 4 Value");

        // Create card based on input
        Attachment cardAttachment;
        if ("herocard".equalsIgnoreCase(cardType)) {
            // Create HeroCard with buttons
            HeroCard heroCard = new HeroCard();
            heroCard.setTitle("HERO CARD");
            heroCard.setText("Select from the list of buttons");
            heroCard.setButtons(Arrays.asList(button1, button2, button3));
            cardAttachment = heroCard.toAttachment();
        } else if ("thumbnail".equalsIgnoreCase(cardType)) {
            // Create ThumbnailCard with buttons
            ThumbnailCard thumbnailCard = new ThumbnailCard();
            thumbnailCard.setText("Please select one of the buttons");
            thumbnailCard.setTitle("THUMBNAIL");
            thumbnailCard.setButtons(Arrays.asList(button1, button2, button3));
            cardAttachment = thumbnailCard.toAttachment();
        } else {
            // Default to HeroCard if input is invalid
            HeroCard heroCard = new HeroCard();
            heroCard.setText("You said: "+cardType);
            cardAttachment = heroCard.toAttachment();
        }

        // Create an Activity with the card as an attachment
        Activity activity = MessageFactory.attachment(cardAttachment);
        activity.setType(ActivityTypes.MESSAGE);
        activity.setValue("acitivty-value");
        activity.setValueType("activity-value-type");

        return activity;
    }

    public static Activity createTextMessagesActivity() {
        // Create a list of text messages
        List<String> messages = Arrays.asList(
                "Text Message 1",
                "Text Message 2",
                "Text Message 3"
        );

        List<Attachment> attachments = createAttachments(messages);

        Activity activity = MessageFactory.attachment(attachments);
        activity.setType(ActivityTypes.MESSAGE);
        activity.setAttachmentLayout(AttachmentLayoutTypes.LIST);

        return activity;
    }

    private static List<Attachment> createAttachments(List<String> messages) {
        return messages.stream()
                .map(message -> {
                    HeroCard card = new HeroCard();
                    card.setText(message);
                    return card.toAttachment();
                }).collect(Collectors.toList());
    }

}
