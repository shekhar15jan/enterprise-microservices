package com.enterprise.product.product_service.exceptions;

public class ResourceNotFoundException extends NotFoundException {
    private final String resourceName;
    private final String resourceId;
    public ResourceNotFoundException(String resourceName, String resourceId) {
        super(resourceName +"with id "+resourceId+"not found"); //404
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }
    public String getResourceName() {
        return resourceName;
    }
    public String getResourceId() {
        return resourceId;
    }

    @Override
    public String getErrorCode() {
        return "RES-NOTFOUND-404";
    }

    @Override
    public String getTitle() {
        return resourceName+" Not Found";
    }
}
