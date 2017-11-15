// CSC254
// A2
// Zixiang Liu
// Partner: Yukun Chen

#ifndef __SCAN_H_INCLUDED__
#define __SCAN_H_INCLUDED__

enum token {t_read, t_write, t_id, t_literal, t_gets, t_add, t_sub, t_mul,
			t_div, t_lparen, t_rparen, t_eof, t_if, t_fi, t_do, t_od, t_check,
			t_eq, t_uneq, t_sm, t_lar, t_smeq, t_lareq};
enum ttype {pp, sl, s, id, assign, lp, rp, read, write, tif, fi, tdo, od, 
			check, re, exp, et, tt, ter, ft, fact, lit, pluss, minuss, lsp, aop, 
			mop, rop, eq, uneq, sm, lar, smeq, lareq, times, tdivides};
extern char token_image[100];
extern int pointer;
extern int error_pointer;
extern bool error_flag;

extern token scan();

extern bool myisalpha(int c);
extern bool myisdigit(int c);

#endif
