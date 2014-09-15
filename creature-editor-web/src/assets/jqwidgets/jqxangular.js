/*
jQWidgets v3.5.0 (2014-Sep-15)
Copyright (c) 2011-2014 jQWidgets.
License: http://jqwidgets.com/license/
*/
(function(f, j, g) {
    if (!j) {
        throw new Error("Missing required JavaScript reference for AngularJS");
        return
    }
    f.jqx = f.jqx || {};
    var b = j.module("jqwidgets", []);
    var e = null;
    var q = null;
    var l = null;
    var o = null;
    var k = null;
    var c = new Array();
    var r = new Array();
    var d = {};

    function n(w, x, s, u, t) {
        if (u && t) {
            switch (w) {
                case "jqxGrid":
                case "jqxDataTable":
                case "jqxTreeGrid":
                    if (s.columns) {
                        var z = u.columns || u;
                        var v = t.columns || t;
                        if (z.length != v.length) {
                            return false
                        }
                        var y = {};
                        f.each(z, function(A, C) {
                            var B = this;
                            for (var D in this) {
                                if (B[D] != v[A][D]) {
                                    var E = B.datafield || B.dataField;
                                    if (!y[E]) {
                                        y[E] = {}
                                    }
                                    y[E][D] = B[D]
                                }
                            }
                        });
                        if (!f.isEmptyObject(y)) {
                            f.each(y, function(B, D) {
                                for (var C in D) {
                                    var A = f(x).jqxProxy("getcolumnproperty", B, C);
                                    if (A !== D[C]) {
                                        f(x).jqxProxy("setcolumnproperty", B, C, D[C])
                                    }
                                }
                            });
                            return true
                        }
                    }
                    break
            }
        }
        return false
    }

    function a(w, u, t, y, v) {
        var x = function(B) {
            if (typeof B == "object") {
                if (B._bindingUpdate != null) {
                    return B
                }
            }
            var A = {};
            if (f.isArray(B) || (B instanceof Object && !B.url)) {
                A.localData = B;
                A.type = "array"
            } else {
                if (A.url) {
                    A = B
                }
            }
            var C = new f.jqx.dataAdapter(A);
            return C
        };
        if (w.jqxSource) {
            w.$watchCollection("jqxSource", function(C, B) {
                if (C != B) {
                    var A = x(C);
                    f(u).jqxProxy({
                        source: A
                    })
                }
            })
        } else {
            var s = null;
            for (var z in v) {
                if (w.$parent[z] == w.jqxSettings.source) {
                    s = z;
                    break
                }
            }
            if (s) {
                v.$watchCollection(s, function(C, B) {
                    if (C != B) {
                        var A = x(C);
                        f(u).jqxProxy({
                            source: A
                        })
                    }
                })
            }
            w.$watchCollection("jqxSettings", function(C, B) {
                if (C.source != B.source) {
                    var A = x(C.source);
                    f(u).jqxProxy({
                        source: A
                    })
                }
            })
        }
        return x(y)
    }

    function p(s) {
        if (s == null) {
            return ""
        }
        var t = "";
        f.each(s, function(v) {
            var x = this;
            if (v > 0) {
                t += ", "
            }
            t += "[";
            var u = 0;
            if (f.type(x) == "object") {
                for (var w in x) {
                    if (u > 0) {
                        t += ", "
                    }
                    t += "{" + w + ":" + x[w] + "}";
                    u++
                }
            } else {
                if (u > 0) {
                    t += ", "
                }
                t += "{" + v + ":" + x + "}";
                u++
            }
            t += "]"
        });
        return t
    }

    function h(G, y, F, u, E) {
        var w = j.extend({}, G.$eval("jqxSettings"));
        if (w.source) {
            w.source = a(G, y, F, w.source, E)
        } else {
            if (G.jqxSource) {
                var s = j.extend({}, G.$eval("jqxSource"));
                w.source = a(G, y, F, s, E)
            }
        }
        G.$watch("ngDisabled", function(K, J) {
            if (K != g) {
                if (K != J || f(y).jqxProxy("disabled") !== K) {
                    var I = {};
                    I.disabled = K;
                    f(y).jqxProxy(I)
                }
            }
        });
        var C = {};
        var B = {};
        var t = false;
        if (d[u]) {
            f.each(d[u], function() {
                var K = this.label;
                var L = this.value;
                var J = f.camelCase(L.substring("4"));
                if (typeof G[K] !== "undefined") {
                    var M = G.$eval(K);
                    if (J == "instance") {
                        t = true;
                        return true
                    }
                    C[J] = M;
                    var I = function(R, P) {
                        if (R != P) {
                            var Q = f.camelCase(L.substring("4"));
                            if (Q == "watch") {
                                var T = F.jqxWatch.split(".");
                                for (var S = 0; S < T.length; S++) {
                                    if (T[S] in f(y).data().jqxWidget) {
                                        Q = T[S];
                                        break
                                    }
                                }
                            }
                            var N = {};
                            N[Q] = R;
                            var O = n(u, f(y), N, R, P);
                            if (!O) {
                                f(y).jqxProxy(N)
                            }
                        }
                    };
                    if (J == "watch") {
                        delete C[J];
                        G.$watch(K, I, true)
                    } else {
                        G.$watch(K, I)
                    }
                }
            })
        }
        if (y[0].id == "") {
            if (g == c[u]) {
                c[u] = 0
            }
            y[0].id = u + c[u] ++
        }
        var v = f(y)[u];
        if (!v) {
            throw new Error("Missing required JavaScript references for: " + u);
            return null
        }
        f.each(w, function(I, J) {
            if (I === "data") {
                return true
            }
            if (f.isFunction(J) && !I.match(/(ready|render|renderer|renderToolbar|renderStatusBar|groupsrenderer|pagerrenderer|groupcolumnrenderer|updatefilterconditions|handlekeyboardnavigation|updatefilterpanel|rendered|virtualModeCreateRecords|virtualModeRecordCreating|search|selectionRenderer)/g)) {
                B[I] = J
            } else {
                C[I] = J
            }
        });
        f.extend(f.jqx["_" + u + ""].prototype, {
            definedInstance: function() {
                var I = this;
                f.each(B, function(K, L) {
                    I.addHandler(f(y), K, function(M) {
                        G.$parent ? f.proxy(L, E)(M) : L(M);
                        if (G.$root.$$phase != "$apply" && G.$root.$$phase != "$digest") {
                            G.$apply()
                        }
                    })
                });
                var J = F.$attr;
                f.each(F, function(N, O) {
                    if (N.indexOf("jqxOn") >= 0) {
                        var K = J[N].substring(7);
                        var M = f.camelCase(K);
                        var L = O;
                        I.addHandler(f(y), M, function(Q) {
                            if (L.indexOf("(") >= 0) {
                                var P = L.indexOf("(");
                                if (G.$parent) {
                                    var R = E[L.substring(0, P)];
                                    if (R) {
                                        R(Q)
                                    }
                                } else {
                                    if (G[L.substring(0, P)]) {
                                        var R = G[L.substring(0, P)];
                                        if (R) {
                                            R(Q)
                                        }
                                    } else {
                                        G.$emit(M, Q)
                                    }
                                }
                            } else {
                                G.$emit(L, Q)
                            } if (G.$root.$$phase != "$apply" && G.$root.$$phase != "$digest") {
                                G.$apply()
                            }
                        })
                    } else {
                        if (N.indexOf("ngClick") >= 0 || (N.indexOf("ngDblclick") >= 0)) {
                            if (E != G.$parent) {
                                var K = J[N].substring(3);
                                var M = f.camelCase(K);
                                var L = O;
                                I.addHandler(f(y), M, function(Q) {
                                    if (L.indexOf("(") >= 0) {
                                        var P = L.indexOf("(");
                                        if (G.$parent) {
                                            var R = E[L.substring(0, P)];
                                            if (R) {
                                                R(Q)
                                            }
                                        } else {
                                            if (G[L.substring(0, P)]) {
                                                var R = G[L.substring(0, P)];
                                                if (R) {
                                                    R(Q)
                                                }
                                            } else {
                                                G.$emit(M, Q)
                                            }
                                        }
                                    } else {
                                        G.$emit(L, Q)
                                    } if (G.$root.$$phase != "$apply" && G.$root.$$phase != "$digest") {
                                        G.$apply()
                                    }
                                })
                            }
                        }
                    }
                });
                if (F.jqxInstance) {
                    G.jqxInstance = I;
                    if (E[F.jqxInstance] != I) {
                        E[F.jqxInstance] = I
                    }
                    if (G.$root.$$phase != "$apply" && G.$root.$$phase != "$digest") {
                        G.$apply()
                    }
                }
            }
        });
        var D = y[0];
        if (G.jqxSettings) {
            if (!G.jqxSettings.apply) {
                G.jqxSettings.apply = G.jqxSettings[u] = function() {
                    var I = arguments;
                    var J = new Array();
                    if (I.length == 0) {
                        return true
                    }
                    f.each(r[u], function(K, L) {
                        var M = this;
                        J.push({
                            widgetName: u,
                            element: M,
                            result: f.jqx.jqxWidgetProxy(u, M, I)
                        })
                    });
                    if (J.length == 1) {
                        return J[0].result
                    }
                    return J
                };
                r[u] = new Array();
                r[u].push(D)
            } else {
                if (!r[u]) {
                    r[u] = new Array()
                }
                r[u].push(D)
            }
        }
        var A = {};
        var z = "";
        for (var z in G) {
            if (z.indexOf("jqx") >= 0 && z != "jqxInstance") {
                A[z] = j.copy(G[z])
            }
        }
        var x = f(y)[u](C);
        var H = f(y)[u]("getInstance");
        var z = "";
        for (var z in A) {
            G[z] = A[z]
        }
        if (G.jqxSettings) {
            G.$watch("jqxSettings", function(M, L) {
                var J = {};
                var I = false;
                if (M != L) {
                    f.each(M, function(O, Q) {
                        if (O === "source") {
                            if (L != null) {
                                return true
                            } else {
                                var N = a(G, y, F, Q, E);
                                J[O] = N
                            }
                        }
                        if (O === "data") {
                            G.$apply();
                            return true
                        }
                        var P = H.events || H._events;
                        if ((P && P.indexOf(O) >= 0) || O.match(/(mousedown|click|mouseenter|mouseleave|mouseup|keydown|keyup|focus|blur|keypress)/g)) {
                            return true
                        }
                        if (!(Q instanceof Object) && (L == null || Q !== L[O])) {
                            J[O] = Q;
                            I = true
                        } else {
                            if (O !== u && O !== "apply" && (Q instanceof Object) && (L == null || (p(Q) !== p(L[O])) || (p(Q) == "" && p(L[O]) == ""))) {
                                J[O] = Q;
                                I = true
                            }
                        }
                    });
                    if (J !== {} && I) {
                        var K = n(u, f(y), J, M, L);
                        if (!K) {
                            f(y).jqxProxy(J)
                        }
                    }
                }
            })
        }
        G.$on("$destroy", function() {
            f(y)[u]("destroy")
        });
        return H
    }

    function m(u) {
        var z = u[0].nodeName.toLowerCase();
        var v = f(u).parent();
        var w = f(u).html();
        var x = '<div id="jqx-ngwidget">' + w + "</div>";
        if (z.indexOf("jqx") >= 0) {
            var s = u[0].attributes;
            var y = u;
            if (z.indexOf("input") >= 0) {
                if (z.indexOf("date") >= 0 || z.indexOf("number") >= 0) {
                    f(u).replaceWith('<div id="jqx-ngwidget"></div>')
                } else {
                    if (z.indexOf("password") >= 0) {
                        f(u).replaceWith('<input id="jqx-ngwidget" type="password"/>')
                    } else {
                        f(u).replaceWith('<input id="jqx-ngwidget"/>')
                    }
                }
            } else {
                if (z.indexOf("jqx-button") >= 0 && z.indexOf("jqx-button-group") == -1) {
                    f(u).replaceWith('<button id="jqx-ngwidget">' + w + "</button>")
                } else {
                    if (z.indexOf("jqx-toggle-button") >= 0) {
                        f(u).replaceWith('<button id="jqx-ngwidget">' + w + "</button>")
                    } else {
                        if (z.indexOf("jqx-link-button") >= 0) {
                            f(u).replaceWith('<a id="jqx-ngwidget">' + w + "</a>")
                        } else {
                            if (z.indexOf("jqx-data-table") >= 0) {
                                if (f(u).find("tr").length > 0) {
                                    f(u).replaceWith('<table id="jqx-ngwidget"></table>')
                                } else {
                                    f(u).replaceWith('<div id="jqx-ngwidget"></div>')
                                }
                            } else {
                                if (z.indexOf("jqx-list-box") >= 0 || z.indexOf("jqx-drop-down-list") >= 0 || z.indexOf("jqx-combo-box") >= 0) {
                                    if (f(u).find("option").length > 0) {
                                        f(u).replaceWith('<select id="jqx-ngwidget">' + w + "</select>")
                                    } else {
                                        f(u).replaceWith('<div id="jqx-ngwidget"></div>')
                                    }
                                } else {
                                    if (z.indexOf("jqx-list-menu") >= 0) {
                                        f(u).replaceWith('<ul id="jqx-ngwidget" data-role="listmenu">' + w + "</ul>")
                                    } else {
                                        if (z.indexOf("jqx-tooltip") >= 0) {
                                            var t = f(u).children();
                                            t.detach();
                                            f(t).insertAfter(f(u));
                                            f.each(s, function() {
                                                if (f(t)[0]) {
                                                    f(t)[0].setAttribute(this.name, this.value)
                                                }
                                            });
                                            f(u).remove()
                                        } else {
                                            f(u).replaceWith(x)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            y = v.find("#jqx-ngwidget").removeAttr("id");
            f.each(s, function() {
                if (f(y)[0]) {
                    f(y)[0].setAttribute(this.name, this.value)
                }
            })
        }
    }

    function i(s) {
        if (f.fn && !f.fn[s]) {
            return
        }
        b.directive(s, ["$timeout", "$interval", "$parse", "$compile", "$log",
            function(x, u, y, w, v) {
                q = x;
                l = u;
                e = y;
                o = w;
                k = v;
                var t;
                return {
                    restrict: "ACE",
                    require: ["?ngModel"],
                    scope: {
                        jqxNgModel: "=",
                        jqxSettings: "=",
                        jqxSource: "=",
                        ngDisabled: "=",
                        jqxCreate: "=",
                        data: "=jqxData"
                    },
                    template: function(A, z) {
                        m(A);
                        var B = this;
                        f.each(z, function(C, D) {
                            if (C !== s && C != "jqxNgModel" && C.indexOf("jqxOn") == -1 && C != "jqxData" && C != "jqxSource" && C != "jqxCreate" && C != "jqxSettings" && C.indexOf("jqx") >= 0) {
                                B.scope[C] = "=";
                                if (!d[s]) {
                                    d[s] = new Array()
                                }
                                d[s].push({
                                    label: C,
                                    value: z.$attr[C]
                                })
                            }
                        });
                        t = this.scope
                    },
                    controller: ["$scope", "$attrs", "$element", "$transclude",
                        function(C, z, B, A) {
                            o(B.contents())
                        }
                    ],
                    link: function(J, D, I, C, A) {
                        var B = D[0].style.visibility;
                        var F = D[0].style.display;
                        D[0].style.visibility = "hidden";
                        D[0].style.display = "none";
                        var z = 0;
                        var E = function(K) {
                            if ("jqxCreate" in K.$parent) {
                                z++;
                                return E(K.$parent)
                            } else {
                                if (z == 0) {
                                    return K.$parent
                                }
                            } if (K) {
                                return K.$parent
                            } else {
                                return null
                            }
                        };
                        var H = E(J);
                        if (H && z > 0) {
                            f.each(I, function(K) {
                                if (K.indexOf("jqx") >= 0) {
                                    if (K.indexOf("jqxOn") >= 0) {
                                        return true
                                    }
                                    J[K] = H.$eval(I[K]);
                                    H.$watch(I[K], function(M, L) {
                                        if (J[K] !== M) {
                                            J[K] = M
                                        }
                                    })
                                }
                            })
                        }
                        var G = l(function() {
                            l.cancel(G);
                            G = g;
                            var L = function() {
                                var Q = C[0];
                                D[0].style.visibility = B;
                                D[0].style.display = F;
                                var P = h(J, D, I, s, H);
                                var M = s.toLowerCase();
                                var O = s.match(/(input|list|radio|checkbox|combobox|rating|slider|scrollbar|progress|range|editor|picker|range|gauge|calendar|switch|button)/ig);
                                var N = {
                                    element: D[0],
                                    name: s,
                                    id: D[0].id
                                };
                                J.$emit(s + "Created", N);
                                q(function R() {
                                    if (Q) {
                                        Q.$render = function() {
                                            var U = Q.$viewValue;
                                            if (U === g) {
                                                U = Q.$modelValue
                                            }
                                            if (s === "jqxRadioButton") {
                                                return
                                            }
                                            if (s === "jqxCheckBox") {
                                                return
                                            }
                                            if (U != f(D).val()) {
                                                q(function() {
                                                    f(D).val(U)
                                                })
                                            }
                                        };
                                        if (O) {
                                            var T = "keyup change";
                                            if (s == "jqxScrollBar") {
                                                T = "valueChanged"
                                            }
                                            f(D).on(T, function(V) {
                                                var U = V.args;
                                                q(function() {
                                                    if (s === "jqxRadioButton") {
                                                        if (U.type != "api") {
                                                            Q.$setViewValue(J.$eval(f(D).attr("value")))
                                                        }
                                                    } else {
                                                        if (s === "jqxCheckBox") {
                                                            if (f(D).attr("ng-true-value") != g && U.checked) {
                                                                Q.$setViewValue(f(D).attr("ng-true-value"))
                                                            } else {
                                                                if (f(D).attr("ng-false-value") != g && !U.checked) {
                                                                    Q.$setViewValue(f(D).attr("ng-false-value"))
                                                                } else {
                                                                    Q.$setViewValue(f(D).val())
                                                                }
                                                            }
                                                        } else {
                                                            if (s === "jqxDropDownList" || s === "jqxComboBox" || s === "jqxListBox" || s === "jqxInput") {
                                                                var X = f(D).val();
                                                                if (I.jqxNgModel != g) {
                                                                    var W = f(D).data().jqxWidget;
                                                                    if (W.getSelectedItem) {
                                                                        X = W.getSelectedItem();
                                                                        if (X.originalItem) {
                                                                            X = X.originalItem
                                                                        }
                                                                    }
                                                                    if (s === "jqxInput") {
                                                                        X = W.selectedItem
                                                                    }
                                                                    Q.$setViewValue(X)
                                                                } else {
                                                                    Q.$setViewValue(X)
                                                                }
                                                            } else {
                                                                if (s === "jqxDateTimeInput" || s === "jqxCalendar") {
                                                                    if (I.jqxNgModel != g) {
                                                                        var W = f(D).data().jqxWidget;
                                                                        if (W.selectionMode == "range") {
                                                                            Q.$setViewValue(W.getRange())
                                                                        } else {
                                                                            Q.$setViewValue(W.getDate())
                                                                        }
                                                                    } else {
                                                                        Q.$setViewValue(f(D).val())
                                                                    }
                                                                } else {
                                                                    Q.$setViewValue(f(D).val())
                                                                }
                                                            }
                                                        }
                                                    }
                                                })
                                            })
                                        }
                                        if (s === "jqxRadioButton") {
                                            if (J.$eval(f(D).attr("value")) == Q.$viewValue) {
                                                f(D).val(true)
                                            } else {
                                                if (J.$eval(f(D).attr("value")) == "true" && Q.$viewValue == true) {
                                                    f(D).val(true)
                                                } else {
                                                    f(D).val(false)
                                                }
                                            }
                                        } else {
                                            if (s === "jqxCheckBox") {
                                                if (J.$eval(f(D).attr("ng-true-value")) == Q.$viewValue) {
                                                    f(D).val(true)
                                                }
                                                if (J.$eval(f(D).attr("ng-false-value")) == Q.$viewValue) {
                                                    f(D).val(false)
                                                } else {
                                                    f(D).val(Q.$viewValue)
                                                }
                                            } else {
                                                if (s === "jqxDropDownList" || s === "jqxComboBox" || s === "jqxListBox" || s === "jqxInput") {
                                                    if (I.jqxNgModel != g) {
                                                        var S = f(D).data().jqxWidget;
                                                        if (s != "jqxInput") {
                                                            if (S.valueMember) {
                                                                S.selectItem(Q.$viewValue[S.valueMember])
                                                            } else {
                                                                if (S.displayMember) {
                                                                    S.selectItem(Q.$viewValue[S.displayMember])
                                                                } else {
                                                                    f(D).val(Q.$viewValue)
                                                                }
                                                            }
                                                        } else {
                                                            f(D).val(Q.$viewValue)
                                                        }
                                                    } else {
                                                        f(D).val(Q.$viewValue)
                                                    }
                                                } else {
                                                    if (s === "jqxDateTimeInput" || s === "jqxCalendar") {
                                                        if (I.jqxNgModel != g) {
                                                            var S = f(D).data().jqxWidget;
                                                            if (S.selectionMode == "range") {
                                                                S.setRange(Q.$viewValue)
                                                            } else {
                                                                S.setDate(Q.$viewValue)
                                                            }
                                                        } else {
                                                            f(D).val(Q.$viewValue)
                                                        }
                                                    } else {
                                                        f(D).val(Q.$viewValue)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                })
                            };
                            if (J.jqxCreate != null || I.jqxCreate != null) {
                                var K = J.$watch("jqxCreate", function(N, M) {
                                    if (typeof N == "number") {
                                        q(L, N);
                                        K()
                                    } else {
                                        if (N) {
                                            L();
                                            K()
                                        }
                                    }
                                })
                            } else {
                                L()
                            }
                        })
                    }
                }
            }
        ])
    }
    i("jqxBulletChart");
    i("jqxButtonGroup");
    i("jqxButton");
    i("jqxRepeatButton");
    i("jqxToggleButton");
    i("jqxLinkButton");
    i("jqxCalendar");
    i("jqxChart");
    i("jqxCheckBox");
    i("jqxColorPicker");
    i("jqxComboBox");
    i("jqxDataTable");
    i("jqxDateTimeInput");
    i("jqxDocking");
    i("jqxDockPanel");
    i("jqxDragDrop");
    i("jqxDraw");
    i("jqxDropDownButton");
    i("jqxDropDownList");
    i("jqxEditor");
    i("jqxExpander");
    i("jqxGauge");
    i("jqxGrid");
    i("jqxInput");
    i("jqxListBox");
    i("jqxListMenu");
    i("jqxMaskedInput");
    i("jqxMenu");
    i("jqxNavigationBar");
    i("jqxNotification");
    i("jqxNumberInput");
    i("jqxPanel");
    i("jqxPasswordInput");
    i("jqxProgressBar");
    i("jqxRadioButton");
    i("jqxRangeSelector");
    i("jqxRating");
    i("jqxScrollBar");
    i("jqxScrollView");
    i("jqxSlider");
    i("jqxSplitter");
    i("jqxSwitchButton");
    i("jqxTabs");
    i("jqxTooltip");
    i("jqxTouch");
    i("jqxTree");
    i("jqxTreeGrid");
    i("jqxTreeMap");
    i("jqxValidator");
    i("jqxWindow")
})(jqxBaseFramework, window.angular);