package com.devonfw.quarkus.productmanagement.rest.v1;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.general.rest.security.ApplicationAccessControlConfig;
import com.devonfw.quarkus.productmanagement.logic.UcFindProduct;
import com.devonfw.quarkus.productmanagement.logic.UcManageProduct;
import com.devonfw.quarkus.productmanagement.rest.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;

//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/products")
// how we serialize response
@Produces(MediaType.APPLICATION_JSON)
// how we deserialize params
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  // using @Context we can inject contextual info from JAXRS(e.g. http request, current uri info, endpoint info...)
  @Context
  UriInfo uriInfo;

  @Inject
  UcFindProduct ucFindProduct;

  @Inject
  UcManageProduct ucManageProduct;

  @GET
  @PermitAll
  public Page<ProductDto> getAll(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProducts(dto);
  }

  @GET
  @Path("criteriaApi")
  public Page<ProductDto> getAllCriteriaApi(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByCriteriaApi(dto);
  }

  @GET
  @Path("queryDsl")
  public Page<ProductDto> getAllQueryDsl(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByQueryDsl(dto);
  }

  @GET
  @Path("query")
  public Page<ProductDto> getAllQuery(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByTitleQuery(dto);
  }

  @GET
  @Path("nativeQuery")
  public Page<ProductDto> getAllNativeQuery(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByTitleNativeQuery(dto);
  }

  @GET
  @Path("ordered")
  public Page<ProductDto> getAllOrderedByTitle() {

    return this.ucFindProduct.findProductsOrderedByTitle();
  }

  @POST
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_PRODUCT)
  // We did not define custom @Path - so it will use class level path.
  // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
  public ProductDto createNewProduct(NewProductDto dto) {

    return this.ucManageProduct.saveProduct(dto);
  }

  @GET
  @PermitAll
  @Path("{id}")
  public ProductDto getProductById(@PathParam("id") String id) {

    return this.ucFindProduct.findProduct(id);
  }

  @GET
  @Path("title/{title}")
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    return this.ucFindProduct.findProductByTitle(title);
  }

  @DELETE
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_PRODUCT)
  @Path("{id}")
  public ProductDto deleteProductById(@PathParam("id") String id) {

    return this.ucManageProduct.deleteProduct(id);
  }
}
