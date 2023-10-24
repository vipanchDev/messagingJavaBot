// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.vipanch.messaging;

import com.codepoetics.protonpack.collectors.CompletableFutures;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.*;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;

import javax.swing.tree.TreeNode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class implements the functionality of the Bot.
 *
 * <p>
 * This is where application specific logic for interacting with the users would be added. For this
 * sample, the {@link #onMessageActivity(TurnContext)} echos the text back to the user. The {@link
 * #onMembersAdded(List, TurnContext)} will send a greeting to new conversation participants.
 * </p>
 */
public class EchoBot extends ActivityHandler {

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
//        Activity activity = MessageFactory.text("Echo: " + turnContext.getActivity().getText());

        String text = turnContext.getActivity().getText();
        Activity activity = null;
        if(text.equals("buttons")){
            activity = createButtonsActivity();
        }else if(text.equals("text") || text.equals("hi")){
            activity = createTextActivity();
        }else if(text.equals("list")){
            activity = ActivityBuilder.createTextMessagesActivity();
        }
        else{
            activity = ActivityBuilder.createCardActivity(text);
        }

        JsonNode conversationId = TextNode.valueOf(UUID.randomUUID().toString());
        if(!turnContext.getActivity().getConversation().getProperties().containsKey("conversation_id")) {
            activity.setConversation(turnContext.getActivity().getConversation());
            activity.getConversation().setProperties("conversation_id", conversationId);
        }

        return turnContext
                .sendActivity(activity)
                .thenApply(sendResult -> null);
    }

    private Activity createTextActivity() {
        Activity activity = MessageFactory.text("Hi, Vipanch! How are you?");
        activity.setType(ActivityTypes.MESSAGE);
        activity.setValue("Message-Value");
        return activity;
    }

    private static Activity getCardActionActivity(){
        List<CardAction> heroCards = getCardAction();
        Activity activity = MessageFactory.suggestedCardActions(heroCards, "Outer Text");
        return activity;
    }

    private static List<CardAction> getCardAction() {
        CardAction cardAction1 = new CardAction();
        cardAction1.setType(ActionTypes.MESSAGE_BACK);
        cardAction1.setDisplayText("DT-MessageBack");
        cardAction1.setText("T-MESSAGEBACK");
        cardAction1.setTitle("TITLE-MESSAGEBACK");
        cardAction1.setValue("value-MESSAGEBACK");

        CardAction cardAction2 = new CardAction();
        cardAction2.setType(ActionTypes.IM_BACK);
        cardAction2.setDisplayText("DT-IAMBACK");
        cardAction2.setText("T-IAMBACK");
        cardAction2.setTitle("TITLE-IAMBACK");
        cardAction2.setValue("value-IAMBACK");
        return Arrays.asList(cardAction1, cardAction2);
    }

    private static HeroCard getHerocard(){
        HeroCard heroCard = new HeroCard();
        heroCard.setTitle("Choose an option:");
        CardAction cardAction1 = new CardAction();
        cardAction1.setType(ActionTypes.MESSAGE_BACK);
        cardAction1.setDisplayText("DT-MessageBack");
        cardAction1.setText("T-MESSAGEBACK");
        cardAction1.setTitle("TITLE-MESSAGEBACK");
        cardAction1.setValue("value");

        CardAction cardAction2 = new CardAction();
        cardAction2.setType(ActionTypes.IM_BACK);
        cardAction2.setDisplayText("DT-MessageBack");
        cardAction2.setText("T-MESSAGEBACK");
        cardAction2.setTitle("TITLE-MESSAGEBACK");
        cardAction2.setValue("value");

        heroCard.setButtons(Arrays.asList(
                cardAction1,
                cardAction2
        ));
        return heroCard;
    }

//    8NS6fMtFERA4tdl6yUZHO1-us
//    8NS6fMtFERA4tdl6yUZHO1-us


    @Override
    protected CompletableFuture<Void> onMembersAdded(
        List<ChannelAccount> membersAdded,
        TurnContext turnContext
    ) {
        return membersAdded.stream()
            .filter(
                member -> !StringUtils
                    .equals(member.getId(), turnContext.getActivity().getRecipient().getId())
            ).map(channel -> turnContext.sendActivity(MessageFactory.text("Hello and welcome!")))
            .collect(CompletableFutures.toFutureList()).thenApply(resourceResponses -> null);
    }

    public JsonNode jsonNode(String key, String value) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = String.format("{\"%s\": \"%s\"}", key, value);
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }



    public static Activity createButtonsActivity() {
        // Create buttons
        CardAction buttonAction = new CardAction();
        buttonAction.setType(ActionTypes.IM_BACK);
        buttonAction.setTitle("IMB_TITLE");
        buttonAction.setValue("IMB_VALUE");

        CardAction buttonAction1 = new CardAction();
        buttonAction1.setType(ActionTypes.MESSAGE_BACK);
        buttonAction1.setTitle("MSB_TITLE");
        buttonAction1.setValue("MSB_VALUE");

        // Create HeroCard with the button
        HeroCard heroCard = new HeroCard();
        heroCard.setTitle("Hero Card Title");
        heroCard.setButtons(Arrays.asList(buttonAction, buttonAction1));

        // Create an Activity with the HeroCard as an attachment
        Activity activity = MessageFactory.attachment(heroCard.toAttachment());
        activity.setType(ActivityTypes.MESSAGE);
        activity.setValue("Activity-Value");

        // Set the recipient and sender information
//        activity.setRecipient(new ChannelAccount("user-id"));
//        activity.setFrom(new ChannelAccount("bot-id"));
//        activity.setConversation(new ConversationAccount(false, "conversation-id", null));
//        activity.setChannelId("channel-id");

        return activity;
    }

    public static Activity createListActivity() {
        // Create text message
        CardAction textAction = new CardAction();
        textAction.setType(ActionTypes.MESSAGE_BACK);
        textAction.setTitle("Text Message");
        textAction.setValue("Text Message Value");

        // Create button
        CardAction buttonAction = new CardAction();
        buttonAction.setType(ActionTypes.IM_BACK);
        buttonAction.setTitle("Button");
        buttonAction.setValue("Button Value");


        // Create list of messages
        List<CardAction> cardActions = Arrays.asList(textAction, buttonAction);

        // Create ThumbnailCard with the list of messages
        ThumbnailCard thumbnailCard = new ThumbnailCard();
        thumbnailCard.setTitle("List of Messages");
        thumbnailCard.setButtons(cardActions);
        thumbnailCard.setText("Thumbnail Text");


        // Create an Activity with the ThumbnailCard as an attachment
        Activity activity = MessageFactory.attachment(thumbnailCard.toAttachment());
        activity.setType(ActivityTypes.MESSAGE);
        activity.setValue("Activity-Value");

        // Set the recipient and sender information if needed
        // activity.setRecipient(new ChannelAccount("user-id"));
        // activity.setFrom(new ChannelAccount("bot-id"));
        // activity.setConversation(new ConversationAccount(false, "conversation-id", null));
        // activity.setChannelId("channel-id");

        return activity;
    }


}
