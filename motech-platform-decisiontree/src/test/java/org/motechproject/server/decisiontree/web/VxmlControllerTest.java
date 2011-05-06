package org.motechproject.server.decisiontree.web;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.decisiontree.model.Action;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Transition;
import org.motechproject.server.decisiontree.service.DecisionTreeService;
import org.motechproject.server.decisiontree.service.TreeEventProcessor;
import org.motechproject.server.decisiontree.service.TreeNodeLocator;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VxmlControllerTest {
	private final String treeName = "treeName";
    private final String patientId = "pID";
    private final String errorCodeKey = "errorCode";
	private final String transitionPath ="/";

    @InjectMocks
    VxmlController vxmlController = new VxmlController();

    @Mock
    private DecisionTreeService decisionTreeService;

    @Mock
    private TreeEventProcessor treeEventProcessor;

    @Mock
    private HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Before
    public void initMocks() {

        MockitoAnnotations.initMocks(this);
     }

    @Test
    public void nodeTest() {

        Node node = new Node();
        Transition transition = new Transition();
        transition.setDestinationNode(new Node());
        node.addTransition("1", transition);


        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn("1");
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(node);

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, transitionPath);
        assertEquals(VxmlController.MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());

    }

    @Test
    public void nodeTestNoTransitionWithGivenKey() {

        String transitionKey = "1";

        Node node = new Node();
        Transition transition = new Transition();
        transition.setDestinationNode(new Node());
        node.addTransition("2", transition);

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn(transitionKey);
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(node);

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, transitionPath);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.INVALID_TRANSITION_KEY, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void nodeTestInvalidTransitionKeyType() {

        String transitionKey ="1";


        Node destinationNode = new Node();
        Transition transition1 = new Transition();
        transition1.setDestinationNode(new Node());
        destinationNode.addTransition("a", transition1);

        Node node = new Node();
        Transition transition = new Transition();
        transition.setDestinationNode(destinationNode);
        node.addTransition(transitionKey, transition);



        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn(transitionKey);
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(node);

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, transitionPath);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.INVALID_TRANSITION_KEY_TYPE, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void nodeTestInvalidTransitionNoDestinationNode() {

        Node node = new Node();
        Transition transition = new Transition();
        node.addTransition("1", transition);

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn("1");
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(node);

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, transitionPath);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.NULL_DESTINATION_NODE, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void nodeTestNoTree() {

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn("1");
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(null);

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, transitionPath);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.GET_NODE_ERROR, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void nodeTestException () {

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn("1");
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));
        when(decisionTreeService.getNode(treeName, transitionPath)).thenThrow(new RuntimeException());

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, transitionPath);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.GET_NODE_ERROR, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void rootNodeTest() {

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(decisionTreeService.getNode(treeName, TreeNodeLocator.PATH_DELIMITER)).thenReturn(new Node());

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, TreeNodeLocator.PATH_DELIMITER);
        assertEquals(VxmlController.MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());

    }

    @Test
    public void rootNodeTestNoNode() {

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(null);

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, TreeNodeLocator.PATH_DELIMITER);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.GET_NODE_ERROR, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void rootNodeTestException() {
    	
        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(decisionTreeService.getNode(treeName, transitionPath)).thenThrow(new RuntimeException());

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService).getNode(treeName, TreeNodeLocator.PATH_DELIMITER);
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.GET_NODE_ERROR, modelAndView.getModel().get(errorCodeKey));

    }

    @Test
    public void nodeTestMissingParameters() {

        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService,times(0)).getNode(anyString(), anyString());
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.NULL_PATIENTID_LANGUAGE_OR_TREENAME_PARAM, modelAndView.getModel().get(errorCodeKey));
    }

    @Test
    public void nodeTestMissingTransitionPathsParameter() {

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn("PATIENT_ID");
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn("1");


        ModelAndView modelAndView = vxmlController.node(request, response);

        assertNotNull(modelAndView);
        verify(decisionTreeService, times(0)).getNode(anyString(), anyString());
        assertEquals(VxmlController.ERROR_MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());
        assertEquals(VxmlController.Errors.NULL_TRANSITION_PATH_PARAM, modelAndView.getModel().get(errorCodeKey));
    }

    @Test
    public void sendActionsBeforeTest() {

        Node node = new Node();
        String transitionKey = "1";

        Node parentNode = new Node();
        Transition transition = new Transition();
        transition.setDestinationNode(node);
        parentNode.addTransition(transitionKey, transition);


        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn(patientId);
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn(transitionKey);
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(parentNode);

        ModelAndView modelAndView = vxmlController.node(request, response);

        verify(treeEventProcessor).sendActionsBefore(node, TreeNodeLocator.PATH_DELIMITER + transitionKey, patientId);
        verify(treeEventProcessor, times(0)).sendActionsBefore(parentNode, TreeNodeLocator.PATH_DELIMITER, patientId);
        verify(treeEventProcessor, times(0)).sendActionsAfter(node, TreeNodeLocator.PATH_DELIMITER + transitionKey, patientId);

    }

@Test
    public void sendActionsAfterTest() {

        Node node = new Node();
        String transitionKey = "1";

        Node parentNode = new Node();
        Transition transition = new Transition();
        transition.setDestinationNode(node);
        parentNode.addTransition(transitionKey, transition);


        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn(patientId);
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn(transitionKey);
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(parentNode);

        ModelAndView modelAndView = vxmlController.node(request, response);

        verify(treeEventProcessor, times(0)).sendActionsBefore(parentNode, TreeNodeLocator.PATH_DELIMITER, patientId);
        verify(treeEventProcessor, times(1)).sendActionsAfter(parentNode, TreeNodeLocator.PATH_DELIMITER, patientId);

    }


    @Test
    public void sendTransitionActionsTest() {

        String patientId = "PATIENT_ID";

        Node node = new Node();
        Transition transition = new Transition();
        transition.setDestinationNode(new Node());
        node.addTransition("1", transition);


        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn(patientId);
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(request.getParameter(VxmlController.TRANSITION_KEY_PARAM)).thenReturn("1");
        when(request.getParameter(VxmlController.TRANSITION_PATH_PARAM)).thenReturn(Base64.encodeBase64URLSafeString(transitionPath.getBytes()));

        when(decisionTreeService.getNode(treeName, transitionPath)).thenReturn(node);

        ModelAndView modelAndView = vxmlController.node(request, response);

        verify(treeEventProcessor).sendTransitionActions(transition, patientId);
        assertEquals(VxmlController.MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());

    }


    @Test
    public void sendActionsBeforeRootTest() {

        String patientId = "PATIENT_ID";
        Node node = new Node();

        when(request.getParameter(VxmlController.TREE_NAME_PARAM)).thenReturn(treeName);
        when(request.getParameter(VxmlController.PATIENT_ID_PARAM)).thenReturn(patientId);
        when(request.getParameter(VxmlController.LANGUAGE_PARAM)).thenReturn("en");
        when(decisionTreeService.getNode(treeName, TreeNodeLocator.PATH_DELIMITER)).thenReturn(node);

        ModelAndView modelAndView = vxmlController.node(request, response);

        verify(treeEventProcessor).sendActionsBefore(node, TreeNodeLocator.PATH_DELIMITER, patientId);
        verify(treeEventProcessor, times(0)).sendActionsAfter(node, TreeNodeLocator.PATH_DELIMITER, patientId);

        verify(decisionTreeService).getNode(treeName, TreeNodeLocator.PATH_DELIMITER);
        assertEquals(VxmlController.MESSAGE_TEMPLATE_NAME, modelAndView.getViewName());

    }
}
