package net.shopxx;

import net.shopxx.util.SpringUtils;

public class Message
{
  private Message.Type type;
  private String content;
  
  public Message() {}
  
  public Message(Message.Type type, String content)
  {
    this.type = type;
    this.content = content;
  }
  
  public Message(Message.Type type, String content, Object... args)
  {
    this.type = type;
    this.content = SpringUtils.getMessage(content, args);
  }
  
  public static Message success(String content, Object... args)
  {
    return new Message(Message.Type.success, content, args);
  }
  
  public static Message warn(String content, Object... args)
  {
    return new Message(Message.Type.warn, content, args);
  }
  
  public static Message error(String content, Object... args)
  {
    return new Message(Message.Type.error, content, args);
  }
  
  public Message.Type getType()
  {
    return this.type;
  }
  
  public void setType(Message.Type type)
  {
    this.type = type;
  }
  
  public String getContent()
  {
    return this.content;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public String toString()
  {
    return SpringUtils.getMessage(this.content, new Object[0]);
  }
  
  public enum Type
  {
    success,  warn,  error;
  }
}