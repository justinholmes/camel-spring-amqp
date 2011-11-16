/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Original Code is camel-spring-amqp.
 * 
 * The Initial Developer of the Original Code is Bluelock, LLC.
 * Copyright (c) 2007-2011 Bluelock, LLC. All Rights Reserved.
 */
package amqp.spring.camel.component;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

public class SpringAMQPMessageTest {
    @Test
    public void fromAMQP() throws Exception {
        String body = "Test Message";
        MessageConverter msgConverter = new StringMessageConverter();
        MessageProperties properties = new MessageProperties();
        properties.setHeader("NotSecret", "Popcorn");
        org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(body.getBytes(), properties);
        
        SpringAMQPMessage camelMessage = SpringAMQPMessage.fromAMQPMessage(msgConverter, message);
        Assert.assertEquals(body, camelMessage.getBody(String.class));
        Assert.assertEquals("Popcorn", camelMessage.getHeader("NotSecret"));
    }
    
    @Test
    public void toAMQP() throws Exception {
        MessageConverter msgConverter = new StringMessageConverter();
        SpringAMQPMessage camelMessage = new SpringAMQPMessage();
        camelMessage.setBody("Test Message 2");
        camelMessage.setHeader("Secret", "My Secret");
        
        org.springframework.amqp.core.Message message = camelMessage.toAMQPMessage(msgConverter);
        Assert.assertEquals("Test Message 2", new String(message.getBody()));
        Assert.assertEquals("My Secret", camelMessage.getHeader("Secret"));
    }
    
    private static class StringMessageConverter extends AbstractMessageConverter {
        @Override
        protected Message createMessage(Object object, MessageProperties messageProperties) {
            return new Message(((String) object).getBytes(), messageProperties);
        }

        @Override
        public Object fromMessage(Message message) throws MessageConversionException {
            return new String(message.getBody());
        }
    }
}