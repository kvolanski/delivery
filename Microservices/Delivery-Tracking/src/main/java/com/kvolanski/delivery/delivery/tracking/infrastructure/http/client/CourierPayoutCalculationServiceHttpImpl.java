package com.kvolanski.delivery.delivery.tracking.infrastructure.http.client;

import com.kvolanski.delivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceHttpImpl implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        try {
            var courierPayoutResultModel = courierAPIClient.payoutCalculation(new CourierPayoutCalculationInput(distanceInKm));
            return courierPayoutResultModel.getPayoutFee();
        }catch(ResourceAccessException resourceAccessException){
            throw new GatewayTimeoutException(resourceAccessException);
        }catch (HttpServerErrorException | CallNotPermittedException | IllegalArgumentException exception){
            throw new BadGatewayException(exception);
        }
    }
}
