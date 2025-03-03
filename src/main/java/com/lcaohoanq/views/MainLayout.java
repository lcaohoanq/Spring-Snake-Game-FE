package com.lcaohoanq.views;


import com.lcaohoanq.enums.UserRoleEnum;
import com.lcaohoanq.views.admin.MenuManagement;
import com.lcaohoanq.views.admin.UsersManagement;
import com.lcaohoanq.views.admin.datagrid.DataGridView;
import com.lcaohoanq.views.admin.gridwithfilters.GridwithFiltersView;
import com.lcaohoanq.views.admin.masterdetail.MasterDetailView;
import com.lcaohoanq.views.forgotpassword.ForgotPasswordView;
import com.lcaohoanq.views.home.HomeView;
import com.lcaohoanq.views.menu.GameMenuView;
import com.lcaohoanq.views.scores.ScoresView;
import com.lcaohoanq.views.user.login.UsersLoginView;
import com.lcaohoanq.views.user.register.UsersRegisterView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private Header header;

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER,
                Padding.Horizontal.SMALL,
                TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }

    public MainLayout() {
        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);
        refreshHeader();
        return header;
    }

    private void refreshHeader() {
        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1("Snake Game");
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(appName);

        Nav nav = new Nav();
        nav.addClassNames(Display.FLEX, FlexDirection.ROW, JustifyContent.END, Overflow.AUTO,
            Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }

        H3 username = new H3();
        VaadinSession session = VaadinSession.getCurrent();
        UserRoleEnum role = (UserRoleEnum) session.getAttribute("role");
        if (session.getAttribute("user") != null && role != null) {
            Button logoutButton = new Button("Logout", LineAwesomeIcon.SIGN_OUT_ALT_SOLID.create(),
                event -> {
                    VaadinSession.getCurrent().getSession().invalidate();
                    VaadinSession.getCurrent().close();
                    UI.getCurrent().getPage().setLocation("users/login");
                });
            String user = (String) session.getAttribute("user");
            username.setText(role == UserRoleEnum.ADMIN ? "Admin, " + user : "Welcome, " + user);
            layout.add(username);
            layout.add(logoutButton);
        }

        header.add(layout, nav);
    }

    private MenuItemInfo[] createMenuItems() {
        VaadinSession session = VaadinSession.getCurrent();
        boolean isLoggedIn = session.getAttribute("user") != null;
        UserRoleEnum role = (UserRoleEnum) session.getAttribute("role");

        if (isLoggedIn) {
            if (role == UserRoleEnum.ADMIN) {
                return new MenuItemInfo[]{ //
                    new MenuItemInfo("Menu", LineAwesomeIcon.HOUSE_DAMAGE_SOLID.create(),
                        MenuManagement.class), //

                    new MenuItemInfo("Manage Employees", LineAwesomeIcon.KEY_SOLID.create(),
                        UsersManagement.class), //

                    new MenuItemInfo("Master Details", LineAwesomeIcon.GET_POCKET.create(),
                        MasterDetailView.class), //

                    new MenuItemInfo("Grid", LineAwesomeIcon.BOXES_SOLID.create(),
                        DataGridView.class), //

                    new MenuItemInfo("Grid with Filter", LineAwesomeIcon.FILE_ALT_SOLID.create(),
                        GridwithFiltersView.class), //
                };
            } else if(role == UserRoleEnum.EMPLOYEE) {
                return new MenuItemInfo[]{ //
                    new MenuItemInfo("Manage Users", LineAwesomeIcon.KEY_SOLID.create(),
                        UsersManagement.class), //
                };
            } else {
                return new MenuItemInfo[]{ //
                    new MenuItemInfo("Home", LineAwesomeIcon.HOUSE_DAMAGE_SOLID.create(),
                        HomeView.class), //

                    new MenuItemInfo("Game Menu", LineAwesomeIcon.GAMEPAD_SOLID.create(),
                        GameMenuView.class), //

                    new MenuItemInfo("Scores", LineAwesomeIcon.LIST_SOLID.create(),
                        ScoresView.class), //

                };
            }
        } else {
            return new MenuItemInfo[]{ //
                new MenuItemInfo("Home", LineAwesomeIcon.HOUSE_DAMAGE_SOLID.create(),
                    HomeView.class), //

                new MenuItemInfo("Login", LineAwesomeIcon.KEY_SOLID.create(), UsersLoginView.class),
                //

                new MenuItemInfo("Register", LineAwesomeIcon.KEY_SOLID.create(),
                    UsersRegisterView.class), //

                new MenuItemInfo("Forgot Password", LineAwesomeIcon.KEY_SOLID.create(),
                    ForgotPasswordView.class), //
            };
        }
    }

}
