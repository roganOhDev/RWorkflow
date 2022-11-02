package com.source.rworkflow;

import com.source.rworkflow.common.exception.ExceptionCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class CustomAssertions {
//    public static Error assertThrows(ExceptionCode expected, Executable executable) {
//        try {
//            executable.execute();
//
//            Assertions.fail("Executable did not throw exception");
//        } catch (StatusRuntimeException statusRuntimeException) {
//            final var status = statusRuntimeException.getStatus();
//
//            Assertions.assertEquals(Status.Code.ABORTED, status.getCode());
//
//            if (status.getCode() == Status.Code.ABORTED) {
//                final var error = GrpcError.deserialize(status.getDescription());
//                final var actual = error.getCode();
//
//                Assertions.assertEquals(expected, actual);
//
//                return error;
//            } else {
//                Assertions.fail(statusRuntimeException);
//            }
//        } catch (Throwable throwable) {
//            Assertions.fail(throwable);
//        }
//
//        return Error.getDefaultInstance();
//    }
}
