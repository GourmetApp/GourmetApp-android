package com.jugarte.gourmet.domine.gourmet;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.NotFoundException;
import com.jugarte.gourmet.helpers.DateHelper;

import java.util.List;

public class GetGourmet {

    private static final int SERVICE_OK = 1;
    private static final int SERVICE_NOT_CONNECTION = 2;
    private static final int SERVICE_USER_NOT_FOUND= 3;
    private static final int FIREBASE = 4;

    private static final int ALL_OK = 5;
    private static final int NO_CONNECTION = 6;
    private static final int NOT_FOUND = 7;

    private int numResponse;
    private ChequeGourmet resultService;
    private Gourmet resultFirebase;

    private OnGourmetResponse response;
    private Gourmet finalGourmet;


    public interface OnGourmetResponse {
        void success(Gourmet gourmet);
        void notConnection(Gourmet cacheGourmet);
        void notUserFound();
    }

    public void execute(String user, String password, final OnGourmetResponse response) {
        numResponse = 0;
        this.response = response;

        new GetChequeGourmet().execute(user, password, new GetChequeGourmet.OnChequeGourmetResponse() {
            @Override
            public void success(ChequeGourmet chequeGourmet) {
                resultService = chequeGourmet;
                numResponse += SERVICE_OK;
                checkMerge();
            }

            @Override
            public void error(Exception exception) {
                if (exception instanceof ConnectionException) {
                    numResponse += SERVICE_NOT_CONNECTION;
                } else if (exception instanceof NotFoundException) {
                    numResponse += SERVICE_USER_NOT_FOUND;
                }
                checkMerge();
            }
        });

        new GetGourmetFirebase().execute(user, new GetGourmetFirebase.OnFirebaseResponse() {
            @Override
            public void success(Gourmet gourmet) {
                resultFirebase = gourmet;
                numResponse += FIREBASE;
                checkMerge();
            }

            @Override
            public void error(Exception exception) {
                numResponse += FIREBASE;
                checkMerge();
            }
        });
    }

    private void checkMerge() {
        if (numResponse == ALL_OK) {
            merge();
            finalGourmet.setModificationDate(DateHelper.getCurrentDateTime());
            response.success(finalGourmet);
        } else if (numResponse == NOT_FOUND) {
            response.notUserFound();
        } else if (numResponse == NO_CONNECTION) {
            merge();
            response.notConnection(finalGourmet);
        }
    }

    private void merge() {
        if (checkResultFirebase() && checkResultService()) {
            twoResults();
        } else if (checkResultFirebase()) {
            firebaseResult();
        } else if (checkResultService()) {
            serviceResult();
        }

        if (finalGourmet != null && finalGourmet.getOperations() != null) {
            finalGourmet.orderOperations();
        }
    }

    private void twoResults() {
        finalGourmet = resultFirebase;
        finalGourmet.setCurrentBalance(resultService.getBalance());
        List<Operation> operations = finalGourmet.getOperations();

        for (Operation operation : resultService.getOperations()) {
            if (!containsId(finalGourmet.getOperations(), operation.getId())) {
                operations.add(0, operation);
            }
        }
        finalGourmet.setOperations(operations);
    }

    private void firebaseResult() {
        finalGourmet = resultFirebase;
    }

    private void serviceResult() {
        finalGourmet = new Gourmet();
        finalGourmet.setCardNumber(resultService.getCardNumber());
        finalGourmet.setCurrentBalance(resultService.getBalance());
        finalGourmet.setOperations(resultService.getOperations());
    }

    private boolean checkResultService() {
        return resultService != null && resultService.getOperations() != null;
    }

    private boolean checkResultFirebase() {
        return resultFirebase != null && resultFirebase.getOperations() != null;
    }

    private static boolean containsId(List<Operation> operations, String id) {
        for (Operation operation : operations) {
            if (operation.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
