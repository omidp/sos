/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.sos.messaging.inventory;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Oliver Gierke
 */
public interface Inventory extends CrudRepository<InventoryItem, UUID> {

	/**
	 * Returns the {@link InventoryItem} with the given product identifier.
	 * 
	 * @param id
	 * @return
	 */
	Optional<InventoryItem> findByProductId(UUID id);

	/**
	 * @param productId the identifer of the product to be updated.
	 * @param amount the amount to substract from the current stock.
	 */
	default void updateInventoryItem(UUID productId, long amount) {

		InventoryItem item = findByProductId(productId) //
				.orElseThrow(() -> new IllegalArgumentException("Unknown product!"));

		save(item.decrease(amount));
	}

	default void registerShipment(long amount) {
		findAll().forEach(it -> save(it.increase(amount)));
	}
}
