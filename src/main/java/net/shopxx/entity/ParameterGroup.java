package net.shopxx.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="xx_parameter_group")
public class ParameterGroup
  extends OrderEntity
{
  private static final long serialVersionUID = 192003501177471941L;
  private String IIIllIlI;
  private ProductCategory IIIllIll;
  private List<Parameter> IIIlllII = new ArrayList();
  
  @JsonProperty
  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getName()
  {
    return this.IIIllIlI;
  }
  
  public void setName(String name)
  {
    this.IIIllIlI = name;
  }
  
  @NotNull
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false)
  public ProductCategory getProductCategory()
  {
    return this.IIIllIll;
  }
  
  public void setProductCategory(ProductCategory productCategory)
  {
    this.IIIllIll = productCategory;
  }
  
  @JsonProperty
  @Valid
  @NotEmpty
  @OneToMany(mappedBy="parameterGroup", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL}, orphanRemoval=true)
  @OrderBy("order asc")
  public List<Parameter> getParameters()
  {
    return this.IIIlllII;
  }
  
  public void setParameters(List<Parameter> parameters)
  {
    this.IIIlllII = parameters;
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.entity.ParameterGroup
 * JD-Core Version:    0.7.0.1
 */