//import javax.annotation.Resource;
//import java.io.IOException;
//
//
//        import java.io.IOException;
//        import java.util.ArrayList;
//        import java.util.Arrays;
//        import java.util.List;
//        import java.util.concurrent.Callable;
//        import java.util.concurrent.ExecutionException;
//        import java.util.concurrent.Future;
//        import java.util.concurrent.TimeUnit;
//        import java.util.concurrent.TimeoutException;
//
//        import javax.annotation.Resource;
//        import javax.servlet.http.HttpServletRequest;
//        import javax.servlet.http.HttpServletResponse;
//
//        import io.micrometer.core.instrument.util.StringUtils;
//        import io.swagger.annotations.Api;
//        import io.swagger.annotations.ApiOperation;
//
//        import org.apache.http.HttpResponse;
//        import org.apache.http.HttpStatus;
//        import org.apache.solr.client.solrj.SolrServerException;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.http.HttpMessage;
//        import org.springframework.http.ResponseEntity;
//        import org.springframework.http.codec.HttpMessageEncoder;
//        import org.springframework.web.bind.annotation.PathVariable;
//        import org.springframework.web.bind.annotation.RequestMapping;
//        import org.springframework.web.bind.annotation.RequestMethod;
//        import org.springframework.web.bind.annotation.RequestParam;
//        import org.springframework.web.bind.annotation.ResponseBody;
//        import org.springframework.web.bind.annotation.RestController;
//        import org.springframework.web.client.HttpClientErrorException;
//        import org.springframework.web.client.HttpServerErrorException;
//
//@Api(value="ProductsController", description="Operations pertaining to Products")
//@RequestMapping(value = "/rilfnlwebservices/v2/{baseSiteId}/products")
//@RestController
//public class ProductsController
//{
//    protected static final String DEFAULT_MOBILE_VALUE = "Mobile";
//    protected static final String DEFAULT_PAGE_SIZE = "20";
//    protected static final String DEFAULT_PAGE_SORT = "relevance";
//    protected static final String DEFAULT_CURRENT_PAGE = "0";
//    protected static final String HEADER_TOTAL_COUNT = "X-Total-Count";
//
//    @Autowired
//    SorValueRangeMetaDataServiceImpl SorValueRangeMetaDataServiceImpl;
//
//    @Resource
//    private ProductService service;
//
//    @Resource
//    private AutoSuggestProductService suggestservice;
//
//    @Resource
//    ProductSearchService productSearchService;
//
//    @ApiOperation(value = "API to Check the System Health", response = String.class)
//    @RequestMapping(value = "/health", method = RequestMethod.GET )
//    @ResponseBody()
//    public String getSuggestions(final HttpServletResponse response)
//    {
//        response.setStatus(HttpStatus.SC_OK);
//
//        return "OK";
//    }
//
//    @ApiOperation(value = "API to get the Products list based on the search keyword", response = SuggestionListWsDTO.class)
//    @RequestMapping(value = "/suggestions", method = RequestMethod.GET )
//    @ResponseBody()
//    public SuggestionListWsDTO getSuggestions(@RequestParam(required = true, defaultValue = " ") final String term,
//                                              @RequestParam(required = true, defaultValue = "10") final int max,
//                                              @RequestParam(defaultValue = Constants.DEFAULT_FIELD_SET) final String fields,
//                                              @RequestParam(defaultValue = Constants.DEFAULT_STORE) String store,
//                                              final HttpServletResponse response)
//    {
//        SuggestionListWsDTO result = suggestservice.getAutocompleteSuggestions(term, store, max, fields);
//        response.setHeader("Cache-Control", "public, max-age=1800");
//        return result;
//    }
//
//    /**
//     * @queryparam query Serialized query, free text search, facets.<br>
//     *             The format of a serialized query:
//     *             <b>freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2</b>
//     * @queryparam currentPage The current result page requested.
//     * @queryparam pageSize The number of results returned per page.
//     * @queryparam sort Sorting method applied to the display search results.
//     * @queryparam fields Response configuration (list of fields, which should be returned in response)
//     * @return List of products
//     * @throws CMSItemNotFoundError
//     * @throws InvalidResourceError
//     */
//    @ApiOperation(value = "API to get the Products list by category Code", response = ProductCategorySearchPageWsDTO.class)
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    //@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
//    @ResponseBody
//    public ProductCategorySearchPageWsDTO searchProducts(@RequestParam(required  = false)  String query,
//                                                         @PathVariable final String baseSiteId,
//                                                         @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
//                                                         @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
//                                                         @RequestParam(required = false)  String sort, @RequestParam(defaultValue =  Constants.DEFAULT_FIELD_SET) final String fields,
//                                                         @RequestParam(required = false, defaultValue = DEFAULT_MOBILE_VALUE) final String uiel,
//                                                         @RequestParam(defaultValue = Constants.DEFAULT_STORE) String store,
//                                                         @RequestParam(value = "userGroup", required = false) final String userGroup,
//                                                         @RequestParam(value = "genderFilter", required = false) final String genderFilter,
//                                                         final HttpServletRequest request,
//                                                         final HttpServletResponse response) throws SolrServerException, IOException, FacetSearchException, CMSItemNotFoundError, InvalidResourceError
//    {
//        ProductCategorySearchPageWsDTO result = productSearchService.searchProducts(store,query, baseSiteId, currentPage, pageSize, sort, fields, uiel, userGroup,genderFilter, request, response);
//        response.setHeader("Cache-Control", "public, max-age=1800");
//        return result;
//    }
//
//    /**
//     * Returns a list of products and additional data such as: available facets, available sorting and pagination
//     * options. It can include spelling suggestions.To make spelling suggestions work you need to:
//     *
//     * @queryparam query Serialized query, free text search, facets.<br>
//     *             The format of a serialized query:
//     *             <b>freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2</b>
//     * @queryparam currentPage The current result page requested.
//     * @queryparam pageSize The number of results returned per page.
//     * @queryparam sort Sorting method applied to the display search results.
//     * @queryparam fields Response configuration (list of fields, which should be returned in response)
//     * @return List of products
//     * @throws CMSItemNotFoundError
//     * @throws InvalidResourceError
//     */
//   // @ApiOperation(value = "View a list of product search service available by category", response = ProductCategorySearchPageWsDTO.class)
//    @RequestMapping(value = "category/{categoryCode}", method = RequestMethod.GET)
//    //@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
//    @ResponseBody
//    public
//    ProductCategorySearchPageWsDTO categoryProducts(@PathVariable("categoryCode") String categoryCode,
//                                                    @PathVariable final String baseSiteId,
//                                                    @RequestParam(value = "curated", required = false, defaultValue = "false") boolean curated,
//                                                    @RequestParam(value = "curatedid", required = false) String curatedid,
//                                                    @RequestParam(value = "head", required = false) String head, @RequestParam(required = false) String query,
//                                                    @RequestParam(value = "newin", required = false, defaultValue = "false") boolean newin,
//                                                    @RequestParam(value = "noofdays", required = false) String noofdays,
//                                                    @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
//                                                    @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
//                                                    @RequestParam(required = false) final String sort, @RequestParam(defaultValue = Constants.DEFAULT_FIELD_SET) final String fields,
//                                                    @RequestParam(defaultValue = Constants.DEFAULT_STORE) String store,
//                                                    @RequestParam(value = "userGroup", required = false) final String userGroup,
//                                                    @RequestParam(required = false, defaultValue = DEFAULT_MOBILE_VALUE) final String uiel, final HttpServletRequest request,
//                                                    final HttpServletResponse response) throws SolrServerException, IOException, FacetSearchException, CMSItemNotFoundError, InvalidResourceError
//    {
//        ProductCategorySearchPageWsDTO result = productSearchService.categoryProducts(store,categoryCode, baseSiteId, curated, curatedid, head, query, newin, noofdays, currentPage, pageSize, sort, fields, uiel, userGroup, request, response);
//        response.setHeader("Cache-Control", "public, max-age=1800");
//        return result;
//    }
//
//}
