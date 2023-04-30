package com.console.ticket.cache;

import com.console.ticket.entity.Product;
import com.console.ticket.service.cache.Cache;
import com.console.ticket.service.cache.CacheFactory;
import com.console.ticket.service.cache.CacheType;
import com.console.ticket.util.ProductTestBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LfuCacheTest {
    static CacheFactory instance;
    Cache cacheList;

    @BeforeAll
    static void initialize() {
        instance = new CacheFactory();
    }

    @DisplayName("assert that cache removing most recent used elements")
    @Test
    void checkRemoveOldUsedElementsFromCacheIfNoCapacity() {
        cacheList = instance.getCache(2, CacheType.LFU);

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(2).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();
        Product fishProduct = ProductTestBuilder.aProduct().withId(4).withName("Fish").build();

        //to check if cache delete older objects if no capacity
        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);
        cacheList.put(fishProduct.getId(), fishProduct);

        assertThat((Product) cacheList.get(appleProduct.getId())).isEqualTo(null);
        assertThat((Product) cacheList.get(bananaProduct.getId())).isEqualTo(null);
        assertThat((Product) cacheList.get(cheeseProduct.getId())).isEqualTo(cheeseProduct);
        assertThat((Product) cacheList.get(fishProduct.getId())).isEqualTo(fishProduct);

    }

    @DisplayName("assert that less used object will be replaced by newest if no capacity")
    @Test
    void checkCacheRemoveLessUsedObjectIfNoCapacity() {
        cacheList = instance.getCache(3, CacheType.LFU);

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(2).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();
        Product burgerProduct = ProductTestBuilder.aProduct().withId(4).withName("Burger").build();

        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);

        //to check if products apple and banana will be cached if no capacity
        cacheList.get(appleProduct.getId());
        cacheList.get(bananaProduct.getId());
        cacheList.put(burgerProduct.getId(), burgerProduct);


        assertThat((Product) cacheList.get(appleProduct.getId())).isEqualTo(appleProduct);
        assertThat((Product) cacheList.get(bananaProduct.getId())).isEqualTo(bananaProduct);
        assertThat((Product) cacheList.get(burgerProduct.getId())).isEqualTo(burgerProduct);
        assertThat((Product) cacheList.get(cheeseProduct.getId())).isEqualTo(null);
    }

    @DisplayName("assert that the cache returns null when there is no object with such an ID")
    @Test
    void checkReturnNullIfIdNotExists() {
        cacheList = instance.getCache(3, CacheType.LFU);
        int notExistingIdValue = 139;

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(2).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();

        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);

        //to check if cache return null for not-existing element
        assertThat((Product) cacheList.get(notExistingIdValue)).isEqualTo(null);
    }

    @DisplayName("assert that the get method returns null if cache is empty")
    @Test
    void checkReturnNullIfCacheIsEmpty() {
        cacheList = instance.getCache(10, CacheType.LFU);
        int notExistingIdValue = 1;

        assertThat((Product) cacheList.get(notExistingIdValue)).isEqualTo(null);
    }

    @DisplayName("assert that removed twice element doesn't exists")
    @Test
    void checkCacheBehaviorAfterRemovingElementTwice() {
        cacheList = instance.getCache(2, CacheType.LRU);
        int removingElementId = 2;

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(removingElementId).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();

        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);

        //to check if method return null after double removing the same element
        cacheList.delete(removingElementId);
        cacheList.delete(removingElementId);

        assertThat((Product) cacheList.get(removingElementId)).isEqualTo(null);
    }

    @DisplayName("assert that object with the same id will update value of older")
    @Test
    void checkUpdatedCacheValueAfterSecondPut() {
        cacheList = instance.getCache(3, CacheType.LFU);
        int generalProductId = 13;

        Product bananaProduct = ProductTestBuilder.aProduct().withName("Banana").build();
        Product chocolateProduct = ProductTestBuilder.aProduct().withName("Chocolate").build();
        Product iceCreamProduct = ProductTestBuilder.aProduct().withName("iceCream").build();

        // to check that object with existing id update value of older
        cacheList.put(generalProductId, bananaProduct);
        cacheList.put(generalProductId, chocolateProduct);
        cacheList.put(generalProductId, iceCreamProduct);

        assertThat((Product) cacheList.get(generalProductId)).isEqualTo(iceCreamProduct);
    }
}
