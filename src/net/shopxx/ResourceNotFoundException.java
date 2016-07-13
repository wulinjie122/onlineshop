package net.shopxx;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException
  extends RuntimeException
{
  private static final long serialVersionUID = -9208522773597070069L;
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.ResourceNotFoundException
 * JD-Core Version:    0.7.0.1
 */