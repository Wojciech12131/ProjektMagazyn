import {getCookie} from "./customerPanel";
import {setCookie} from "./customerLogin";
import {delete_cookie} from "./customerPanel";
import {order} from "./customerPanel";
import {denyOrder, acceptOrder} from "./orderPanel";

let cos = test
let val = "123456"

test('requires to be not null and not empty', () => {expect(setCookie(name, val, 1)).toBe(getCookie(name)===val)});
test('its not empty', () => {expect(setCookie(name, val, 1)).toBe(val !== "")});
test('is name of cookie not empty', () => [expect(delete_cookie(name).toBe(name !== ""))]);
test('is id not null', () => {expect(order().toBe(id !== null))});
test ('is id not empty', () => {expect(order().toBe(id !== ""))});
test ('is denyorder returning 0', () => {expect(denyOrder()).toBe(testid === 0)});
test ('is denyorder returning 0', () => {expect(acceptOrder()).toBe(testid === 0)});